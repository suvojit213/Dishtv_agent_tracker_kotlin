package com.example.dishtvagenttracker.data.model

data class CQSummary(
    val month: Int,
    val year: Int,
    val averagePercentage: Double
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