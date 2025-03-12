package com.farmbase.app.sync

import android.util.Log
import com.couchbase.lite.Collection
import com.couchbase.lite.DataSource
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import com.couchbase.lite.Expression
import com.couchbase.lite.Meta
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.Ordering
import com.farmbase.app.database.couchbase.DBManager
import com.farmbase.app.models.FormData
import com.farmbase.app.models.FormInputType
import com.farmbase.app.models.SyncMetadata
import com.farmbase.app.models.UploadComponent
import com.farmbase.app.models.UploadFormData
import com.farmbase.app.models.UploadScreen
import com.farmbase.app.models.UploadSection
import com.farmbase.app.repositories.FormBuilderRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class CouchbaseSyncOrchestrator(
    private val couchbaseManager: DBManager,
    private val repository: FormBuilderRepository
) {
    // Keep track of collection sync configurations
    private val collectionConfigs = ConcurrentHashMap<String, CollectionSyncConfig>()

    // Track sync status for collections
    private val formsCollection: Collection by lazy {
        couchbaseManager.getDatabase().createCollection("forms")
    }

    // Define priorities like in your Room implementation
    data class CollectionSyncConfig(
        val collectionName: String,
        val collectionId: String,
        val batchSize: Int = 50,
        val priority: SyncPriority = SyncPriority.MEDIUM
    ) {
        enum class SyncPriority { HIGH, MEDIUM, LOW }
    }

    /**
     * Register a collection for syncing with specified priority
     */
    private fun getCollectionConfigs(batchSize: Int = 50) {
        try {

            val query = QueryBuilder
                .select(
                    SelectResult.property("id"),
                    SelectResult.property("name"),
                    SelectResult.property("priority")
                )
                .from(DataSource.collection(formsCollection))
                .orderBy(Ordering.expression(Meta.id))

            query.execute().use { resultSet ->
                for (result in resultSet) {
                    val collectionId = result.getString("id")!!
                    val collectionName = result.getString("name")!!
                    val priority =
                        CollectionSyncConfig.SyncPriority.valueOf(result.getString("priority")!!)

                    collectionConfigs[collectionId] = CollectionSyncConfig(
                        collectionName = collectionName,
                        collectionId = collectionId,
                        batchSize = batchSize,
                        priority = priority
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("CouchbaseSync", "Get Collection Configs Failed", e)
        }
    }

    /**
     * Main sync function to orchestrate the syncing process
     */
    suspend fun startSync() = withContext(Dispatchers.IO) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        getCollectionConfigs()

        try {
            // Group collections by priority
            val prioritizedCollections = collectionConfigs.values.groupBy { it.priority }

            // HIGH priority collections (sync first)
            Log.d("CouchbaseSync", "Starting sync for HIGH priority collections")
            val highPriorityJobs = (prioritizedCollections[CollectionSyncConfig.SyncPriority.HIGH]
                ?: emptyList()).map {
                coroutineScope.syncCollection(it)
            }
            highPriorityJobs.awaitAll()

            // MEDIUM priority collections
            Log.d("CouchbaseSync", "Starting sync for MEDIUM priority collections")
            val mediumPriorityJobs =
                (prioritizedCollections[CollectionSyncConfig.SyncPriority.MEDIUM]
                    ?: emptyList()).map {
                    coroutineScope.syncCollection(it)
                }
            mediumPriorityJobs.awaitAll()

            // LOW priority collections
            Log.d("CouchbaseSync", "Starting sync for LOW priority collections")
            val lowPriorityJobs =
                (prioritizedCollections[CollectionSyncConfig.SyncPriority.LOW] ?: emptyList()).map {
                    coroutineScope.syncCollection(it)
                }
            lowPriorityJobs.awaitAll()

            Log.d("CouchbaseSync", "Completed syncing all collections")
        } catch (e: Exception) {
            Log.e("CouchbaseSync", "Sync failed", e)
        }
    }

    /**
     * Sync a specific collection
     */
    private fun CoroutineScope.syncCollection(config: CollectionSyncConfig): Deferred<Unit> {
        return async {
            try {
                // Update sync status to IN_PROGRESS
                updateSyncMetadata(
                    config,
                    SyncMetadata.SyncStatus.IN_PROGRESS,
                )

                // Upload local changes
                uploadChanges(config)

                // Update sync status to COMPLETED
                updateSyncMetadata(
                    config,
                    SyncMetadata.SyncStatus.COMPLETED,
                )
            } catch (e: Exception) {
                // Update sync status to UNSYNCED with error message
                updateSyncMetadata(
                    config,
                    SyncMetadata.SyncStatus.UNSYNCED
                )

                Log.e("CouchbaseSync", "Error syncing collection ${config.collectionName}", e)
                throw e
            }
        }
    }

    /**
     * Upload local changes to server
     */
    private suspend fun uploadChanges(config: CollectionSyncConfig) {
        val collection =
            couchbaseManager.getDatabase().getCollection(config.collectionName) ?: return

        // Get collection form data from forms collection
        val formsQuery = QueryBuilder
            .select(SelectResult.property("json"))
            .from(DataSource.collection(formsCollection))
            .where(Expression.property("id").equalTo(Expression.string(config.collectionId)))

        var formData: FormData?

        formsQuery.execute().use { resultSet ->
            val formDataJson = resultSet.first().getString("json")
            formData = Gson().fromJson(formDataJson, FormData::class.java)
        }

        formData ?: return

        // Get unsynced documents
        var skip = 0
        var hasMoreData = true

        while (hasMoreData) {
            // Query for unsynced documents
            val query = QueryBuilder
                .select(SelectResult.expression(Meta.id))
                .from(DataSource.collection(collection))
                .where(
                    Expression.property("syncStatus").equalTo(Expression.string("UNSYNCED"))
                )
                .limit(
                    Expression.intValue(config.batchSize),
                    Expression.intValue(skip)
                )

            val unsynced = mutableListOf<String>()
            query.execute().use { resultSet ->
                for (result in resultSet) {
                    val docId = result.getString(0)
                    if (docId != null) {
                        unsynced.add(docId)
                    }
                }
            }

            if (unsynced.isEmpty()) {
                hasMoreData = false
                continue
            }

            Log.d(
                "CouchbaseSync",
                "Processing ${unsynced.size} unsynced docs in ${config.collectionName}"
            )

            // Process each unsynced document
            for (docId in unsynced) {
                collection.getDocument(docId)?.let { document ->
                    //Get Form data to upload
                    val uploadFormData = getFormDataToUpload(formData!!, document.toMutable())

                    Log.d("CouchbaseSync", "uploadChanges: $uploadFormData")

                    // Simulate API call
                    val result = repository.uploadFormData(uploadFormData)

                    if (result.isSuccess) {
                        // Update document with sync status
                        val mutableDoc = document.toMutable()
                        mutableDoc.setString("syncStatus", "COMPLETED")
                        collection.save(mutableDoc)
                    }
                }
            }

            skip += config.batchSize
        }
    }

    /**
     * Save sync metadata
     */
    private fun updateSyncMetadata(
        config: CollectionSyncConfig,
        syncStatus: SyncMetadata.SyncStatus,
    ) {
        val metadata = formsCollection.getDocument(config.collectionId)?.toMutable() ?: return

        metadata.setString("syncStatus", syncStatus.name)
        metadata.setLong("lastSyncTime", System.currentTimeMillis())

        formsCollection.save(metadata)
    }

    /**
     * Get sync status for a collection
     */
    fun getSyncStatus(collectionName: String): SyncMetadata.SyncStatus {
        val metadataId = "sync_metadata_$collectionName"
        val statusStr = formsCollection.getDocument(metadataId)?.getString("syncStatus")
            ?: SyncMetadata.SyncStatus.UNSYNCED.name

        return try {
            SyncMetadata.SyncStatus.valueOf(statusStr)
        } catch (e: Exception) {
            SyncMetadata.SyncStatus.UNSYNCED
        }
    }

    private fun getFormDataToUpload(
        formData: FormData,
        formEntryDocument: MutableDocument
    ): UploadFormData {
        // Convert FormData to UploadFormData
        return UploadFormData(
            formId = formData.id,
            userRelatedData = "logged in user data",
            screens = formData.screens.map { screen ->
                UploadScreen(
                    id = screen.id,
                    sections = screen.sections.map { section ->
                        UploadSection(
                            id = section.id,
                            components = section.components.map { component ->
                                UploadComponent(
                                    id = component.id,
                                    label = component.label,
                                    answer = formEntryDocument.getString(component.id)
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}