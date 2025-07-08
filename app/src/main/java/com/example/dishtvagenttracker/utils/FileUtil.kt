package com.example.dishtvagenttracker.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtil {

    fun getReportsDirectory(context: Context): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DishTVReports")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    fun generateFileName(prefix: String, extension: String): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "${prefix}_${timestamp}.${extension}"
    }
}