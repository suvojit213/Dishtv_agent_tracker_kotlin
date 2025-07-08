package com.example.dishtvagenttracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cq_entries")
data class CQEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val auditDate: Date,
    val percentage: Double
) {
    val needsImprovement: Boolean
        get() = percentage < 80.0

    val qualityRating: String
        get() {
            if (percentage >= 95) return "Excellent"
            if (percentage >= 85) return "Good"
            if (percentage >= 75) return "Average"
            if (percentage >= 60) return "Below Average"
            return "Poor"
        }
}