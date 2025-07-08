package com.example.dishtvagenttracker.data.model

import com.example.dishtvagenttracker.core.constants.AppConstants

data class MonthlySummary(
    val month: Int,
    val year: Int,
    val totalLoginHours: Double,
    val totalCalls: Int,
    val csatSummary: CSATSummary? = null
) {
    val formattedMonthYear: String
        get() {
            val monthNames = listOf(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            )
            return "${monthNames[month - 1]} $year"
        }

    val baseSalary: Double
        get() = totalCalls * AppConstants.baseRatePerCall

    val isBonusAchieved: Boolean
        get() = totalCalls >= AppConstants.bonusCallTarget && totalLoginHours >= AppConstants.bonusHourTarget

    val bonusAmount: Double
        get() = if (isBonusAchieved) AppConstants.bonusAmount else 0.0

    val isCSATBonusAchieved: Boolean
        get() = csatSummary != null &&
                csatSummary.monthlyCSATPercentage >= AppConstants.csatBonusPercentage &&
                totalCalls >= AppConstants.csatBonusCallTarget

    val csatBonus: Double
        get() = if (isCSATBonusAchieved) (baseSalary + bonusAmount) * AppConstants.csatBonusRate else 0.0

    val grossSalary: Double
        get() = baseSalary + bonusAmount + csatBonus

    val tdsDeduction: Double
        get() = grossSalary * AppConstants.tdsRate

    val netSalary: Double
        get() = grossSalary - tdsDeduction
}