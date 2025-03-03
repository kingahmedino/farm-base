package com.farmbase.app.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmbase.app.models.Project
import com.farmbase.app.models.SyncMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Insert
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Int): Project?

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getPaginatedProjects(): PagingSource<Int, Project>

    @Query("SELECT * FROM projects WHERE syncStatus = :status LIMIT :limit")
    suspend fun getUnsynced(
        status: SyncMetadata.SyncStatus = SyncMetadata.SyncStatus.UNSYNCED,
        limit: Int = 100
    ): List<Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Update
    suspend fun updateProjects(projects: List<Project>)

    @Query("DELETE FROM projects WHERE id IN (:ids)")
    suspend fun deleteProjects(ids: List<String>)

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun getCount(): Int
}