package com.example.data.repository

import com.example.data.local.CalculationDao
import com.example.data.local.CalculationEntity
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {
    val recentCalculations: Flow<List<CalculationEntity>> = dao.getRecentCalculations()

    suspend fun saveCalculation(entity: CalculationEntity) {
        dao.insertCalculation(entity)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}
