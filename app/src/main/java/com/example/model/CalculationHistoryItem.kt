package com.example.model

data class CalculationHistoryItem(
    val id: Long = System.currentTimeMillis(),
    val expression: String,
    val resultValue: ULong,
    val hexResult: String,
    val decResult: String,
    val binResult: String,
    val wordSize: WordSize,
    val signMode: SignMode,
    val timestamp: Long = System.currentTimeMillis()
)
