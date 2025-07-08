package com.example.dishtvagenttracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dishtvagenttracker.data.model.CQEntry
import com.example.dishtvagenttracker.data.model.CQSummary

@Dao
interface CQEntryDao {
    @Insert
    suspend fun insertCQEntry(cqEntry: CQEntry)

    @Query("SELECT * FROM cq_entries ORDER BY auditDate DESC")
    suspend fun getAllCQEntries(): List<CQEntry>

    @androidx.room.Update
    suspend fun updateCQEntry(cqEntry: CQEntry)

    @androidx.room.Delete
    suspend fun deleteCQEntry(cqEntry: CQEntry)

    @Query("SELECT " +
           "STRFTIME('%m', auditDate / 1000, 'unixepoch') AS month, " +
           "STRFTIME('%Y', auditDate / 1000, 'unixepoch') AS year, " +
           "AVG(percentage) AS averagePercentage " +
           "FROM cq_entries " +
           "GROUP BY month, year " +
           "ORDER BY year DESC, month DESC")
    suspend fun getMonthlyCQSummaries(): List<CQSummary>
}