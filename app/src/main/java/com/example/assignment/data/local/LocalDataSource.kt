package com.example.assignment.data.local

import com.example.assignment.data.local.db.UserHoldingEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveUserHoldings(holdings: List<UserHoldingEntity>)
    fun getUserHoldings(): Flow<List<UserHoldingEntity>>
    suspend fun clearUserHoldings()
}

class LocalDataSourceImpl(
    private val userHoldingDao: com.example.assignment.data.local.db.UserHoldingDao
) : LocalDataSource {
    
    override suspend fun saveUserHoldings(holdings: List<UserHoldingEntity>) {
        userHoldingDao.insertHoldings(holdings)
    }
    
    override fun getUserHoldings(): Flow<List<UserHoldingEntity>> {
        return userHoldingDao.getAllHoldings()
    }
    
    override suspend fun clearUserHoldings() {
        userHoldingDao.clearAllHoldings()
    }
}
