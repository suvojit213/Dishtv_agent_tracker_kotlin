package com.example.dishtvagenttracker.data.model

data class MonthlySummaryRaw(
    val month: Int,
    val year: Int,
    val totalLoginHours: Double,
    val totalCalls: Int
)