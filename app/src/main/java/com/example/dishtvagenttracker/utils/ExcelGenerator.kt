package com.example.dishtvagenttracker.utils

import android.content.Context
import com.example.dishtvagenttracker.data.model.DailyEntry
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object ExcelGenerator {

    fun generateDailyEntriesCsv(context: Context, dailyEntries: List<DailyEntry>): File? {
        val reportsDir = FileUtil.getReportsDirectory(context)
        val fileName = FileUtil.generateFileName("DailyEntries", "csv")
        val file = File(reportsDir, fileName)

        try {
            FileWriter(file).use {
                writer ->
                // Write header
                writer.append("Date,Login Hours,Login Minutes,Login Seconds,Call Count\n")

                // Write data
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                for (entry in dailyEntries) {
                    writer.append("${dateFormat.format(entry.date)}," +
                            "${entry.loginHours}," +
                            "${entry.loginMinutes}," +
                            "${entry.loginSeconds}," +
                            "${entry.callCount}\n")
                }
                writer.flush()
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}