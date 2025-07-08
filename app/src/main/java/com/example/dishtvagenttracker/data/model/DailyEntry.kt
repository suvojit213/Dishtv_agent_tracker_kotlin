package com.example.dishtvagenttracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "daily_entries")
data class DailyEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: Date,
    val loginHours: Int,
    val loginMinutes: Int,
    val loginSeconds: Int,
    val callCount: Int
) {
    // Total login time in seconds
    val totalLoginTimeInSeconds: Int
        get() = (loginHours * 3600) + (loginMinutes * 60) + loginSeconds

    // Total login time in hours (as double for calculations)
    val totalLoginTimeInHours: Double
        get() = loginHours + (loginMinutes / 60.0) + (loginSeconds / 3600.0)
}