package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val resultHex: String,
    val resultDec: String,
    val resultOct: String,
    val resultBin: String,
    val resultRawULong: Long,
    val wordSize: String,
    val signMode: String,
    val timestamp: Long = System.currentTimeMillis()
)
