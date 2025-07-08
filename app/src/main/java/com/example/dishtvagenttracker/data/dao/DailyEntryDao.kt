package com.example.dishtvagenttracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dishtvagenttracker.data.model.DailyEntry
import com.example.dishtvagenttracker.data.model.MonthlySummaryRaw

@Dao
interface DailyEntryDao {
    @Insert
    suspend fun insertDailyEntry(dailyEntry: DailyEntry)

    @Query("SELECT * FROM daily_entries ORDER BY date DESC")
    suspend fun getAllDailyEntries(): List<DailyEntry>

    @androidx.room.Update
    suspend fun updateDailyEntry(dailyEntry: DailyEntry)

    @androidx.room.Delete
    suspend fun deleteDailyEntry(dailyEntry: DailyEntry)

    @Query("SELECT " +
           "STRFTIME('%m', date / 1000, 'unixepoch') AS month, " +
           "STRFTIME('%Y', date / 1000, 'unixepoch') AS year, " +
           "SUM(loginHours + (CAST(loginMinutes AS REAL) / 60.0) + (CAST(loginSeconds AS REAL) / 3600.0)) AS totalLoginHours, " +
           "SUM(callCount) AS totalCalls " +
           "FROM daily_entries " +
           "GROUP BY month, year " +
           "ORDER BY year DESC, month DESC")
    suspend fun getMonthlySummaries(): List<MonthlySummaryRaw>
}