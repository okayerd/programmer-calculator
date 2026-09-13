package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculations ORDER BY timestamp DESC LIMIT 100")
    fun getRecentCalculations(): Flow<List<CalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(entity: CalculationEntity)

    @Query("DELETE FROM calculations")
    suspend fun clearAll()

    @Query("DELETE FROM calculations WHERE id = :id")
    suspend fun deleteById(id: Long)
}
