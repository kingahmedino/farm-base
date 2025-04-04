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

    fun getChildPortfolioData(roleId: String): Flow<String>{
        return roleDao.getChildPortfolioData(roleId)
    }

    fun getRolesById(roles: List<String>): Flow<List<RoleEntity>> {
        return roleDao.getRolesById(roles)
    }

    suspend fun replaceRoles(roles: List<RoleEntity>){
        roleDao.replaceRoles(roles)
    }
}