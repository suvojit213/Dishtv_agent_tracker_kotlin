package com.example.dishtvagenttracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dishtvagenttracker.data.model.CSATEntry
import com.example.dishtvagenttracker.data.model.CSATSummary

@Dao
interface CSATEntryDao {
    @Insert
    suspend fun insertCSATEntry(csatEntry: CSATEntry)

    @Query("SELECT * FROM csat_entries ORDER BY date DESC")
    suspend fun getAllCSATEntries(): List<CSATEntry>

    @androidx.room.Update
    suspend fun updateCSATEntry(csatEntry: CSATEntry)

    @androidx.room.Delete
    suspend fun deleteCSATEntry(csatEntry: CSATEntry)

    @Query("SELECT " +
           "STRFTIME('%m', date / 1000, 'unixepoch') AS month, " +
           "STRFTIME('%Y', date / 1000, 'unixepoch') AS year, " +
           "SUM(t2Count) AS totalT2Count, " +
           "SUM(b2Count) AS totalB2Count, " +
           "SUM(nCount) AS totalNCount, " +
           "(CAST(SUM(t2Count) AS REAL) * 100.0 / (SUM(t2Count) + SUM(b2Count) + SUM(nCount))) - (CAST(SUM(b2Count) AS REAL) * 100.0 / (SUM(t2Count) + SUM(b2Count) + SUM(nCount))) AS monthlyCSATPercentage " +
           "FROM csat_entries " +
           "GROUP BY month, year " +
           "ORDER BY year DESC, month DESC")
    suspend fun getMonthlyCSATSummaries(): List<CSATSummary>
}