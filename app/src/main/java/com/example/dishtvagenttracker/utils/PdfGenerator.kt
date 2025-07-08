package com.example.dishtvagenttracker.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.dishtvagenttracker.data.model.DailyEntry
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object PdfGenerator {

    fun generateDailyEntriesPdf(context: Context, dailyEntries: List<DailyEntry>): File? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.BLACK
        paint.textSize = 12f

        var y = 50f
        val x = 50f
        val lineHeight = 15f

        canvas.drawText("Daily Entries Report", x, y, paint)
        y += lineHeight * 2

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        for (entry in dailyEntries) {
            canvas.drawText("Date: ${dateFormat.format(entry.date)}", x, y, paint)
            y += lineHeight
            canvas.drawText(String.format(
                Locale.getDefault(),
                "Login Time: %02d:%02d:%02d",
                entry.loginHours,
                entry.loginMinutes,
                entry.loginSeconds
            ), x, y, paint)
            y += lineHeight
            canvas.drawText("Call Count: ${entry.callCount}", x, y, paint)
            y += lineHeight * 2
        }

        document.finishPage(page)

        val reportsDir = FileUtil.getReportsDirectory(context)
        val fileName = FileUtil.generateFileName("DailyEntries", "pdf")
        val file = File(reportsDir, fileName)

        try {
            document.writeTo(FileOutputStream(file))
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            document.close()
        }
    }
}