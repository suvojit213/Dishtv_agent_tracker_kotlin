package com.example.dishtvagenttracker.core.constants

object AppConstants {
    // App information
    const val appName = "DishTV Agent Tracker"
    const val appVersion = "1.0.5"
    const val appDeveloper = "Suvojeet"

    // Salary calculation constants
    const val baseRatePerCall = 4.30
    const val bonusAmount = 2000.0
    const val bonusCallTarget = 750
    const val bonusHourTarget = 100 // in hours

    // CSAT Bonus and TDS constants
    const val csatBonusPercentage = 60.0
    const val csatBonusCallTarget = 1000
    const val csatBonusRate = 0.05 // 5% of total salary
    const val tdsRate = 0.10 // 10% TDS

    // Database constants
    const val databaseName = "dishtv_agent_tracker.db"
    const val databaseVersion = 2 // Updated version for CQ feature

    // Table names (not directly used in Room, but for reference)
    const val tableEntries = "daily_entries"
    const val tableCSATEntries = "csat_entries"
    const val tableCQEntries = "cq_entries"

    // Shared preferences keys
    const val prefThemeMode = "theme_mode"
}