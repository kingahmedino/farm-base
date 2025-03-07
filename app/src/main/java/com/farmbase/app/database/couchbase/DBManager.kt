package com.farmbase.app.database.couchbase

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.couchbase.lite.Collection
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.CouchbaseLiteException
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.Document
import com.couchbase.lite.Expression
import com.couchbase.lite.MutableDocument
import com.couchbase.lite.Query
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.ResultSet
import com.couchbase.lite.SelectResult
import java.util.concurrent.atomic.AtomicReference

class DBManager {
    private var database: Database? = null
    private var collection: Collection? = null

    // <.>
    // One-off initialization
    private fun init(context: Context) {
        CouchbaseLite.init(context)
        Log.i(TAG, "CBL Initialized")

    }

    // <.>
    // Create a database
    @Throws(CouchbaseLiteException::class)
    fun createDatabase(dbName: String): Database {
        val database = Database(dbName)
        Log.i(TAG, "Database created: $dbName")
        return database
    }

    // <.>
    // Create a new named collection (like a SQL table)
    // in the database's default scope.
    @Throws(CouchbaseLiteException::class)
    fun Database.createDatabaseCollection(collName: String): Collection {
        val collection = this.createCollection(collName)
        Log.i(TAG, "Collection created: $collection")
        return collection
    }

    // <.>
    // Create a new document (i.e. a record)
    // and save it in a collection in the database.
    @Throws(CouchbaseLiteException::class)
    fun Collection.saveDocument(document: MutableDocument): String {
        this.save(document)
        return document.id
    }

    // <.>
    // Retrieve immutable document and log the database generated
    // document ID and some document properties
    fun Collection.retrieveDoc(docId: String): Document? {
        return this.getDocument(docId)
    }

    // <.>
    // Create a query to fetch documents with language == Kotlin.
    fun Collection.queryDocs(): ResultSet {
        val query: Query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.collection(this))

        return query.execute()
    }

    companion object {
        private const val TAG = "DBManager"

        private val INSTANCE = AtomicReference<DBManager?>()

        @Synchronized
        fun getInstance(context: Context): DBManager {
            var mgr = INSTANCE.get()
            if (mgr == null) {
                mgr = DBManager()
                if (INSTANCE.compareAndSet(null, mgr)) {
                    mgr.init(context)
                }
            }
            return INSTANCE.get()!!
        }
    }
}
