package com.example.dishtvagenttracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "csat_entries")
data class CSATEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: Date,
    val t2Count: Int,
    val b2Count: Int,
    val nCount: Int
) {
    val totalSurveyHits: Int
        get() = t2Count + b2Count + nCount

    val csatPercentage: Double
        get() {
            if (totalSurveyHits == 0) return 0.0
            val t2Score = (100.0 / totalSurveyHits) * t2Count
            val b2Score = (100.0 / totalSurveyHits) * b2Count
            return t2Score - b2Score
        }
}