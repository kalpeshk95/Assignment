package com.example.assignment.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserHoldingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<UserHoldingEntity>)

    @Query("SELECT * FROM user_holdings")
    fun getAllHoldings(): Flow<List<UserHoldingEntity>>

    @Query("DELETE FROM user_holdings")
    suspend fun clearAllHoldings()
}
