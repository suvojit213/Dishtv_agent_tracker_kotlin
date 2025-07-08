package com.example.dishtvagenttracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dishtvagenttracker.data.model.DailyEntry

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
}