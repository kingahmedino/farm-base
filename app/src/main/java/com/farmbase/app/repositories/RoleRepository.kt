package com.farmbase.app.repositories

import com.farmbase.app.database.RoleEntityDao
import com.farmbase.app.models.RoleEntity
import kotlinx.coroutines.flow.Flow

class RoleRepository(
    private val roleDao: RoleEntityDao,
) {
    suspend fun insertRoles(roles: List<RoleEntity>) {
        roleDao.insertRoles(roles)
    }

    fun getAllRoles(): Flow<List<RoleEntity>> {
        return roleDao.selectAllRoles()
    }

    suspend fun replaceRoles(roles: List<RoleEntity>){
        roleDao.replaceRoles(roles)
    }
}