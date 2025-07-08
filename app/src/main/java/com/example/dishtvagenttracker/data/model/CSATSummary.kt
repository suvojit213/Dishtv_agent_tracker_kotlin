package com.example.dishtvagenttracker.data.model

data class CSATSummary(
    val month: Int,
    val year: Int,
    val totalT2Count: Int,
    val totalB2Count: Int,
    val totalNCount: Int,
    val monthlyCSATPercentage: Double
) {
    val formattedMonthYear: String
        get() {
            val monthNames = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            return "${monthNames[month - 1]} $year"
        }
}