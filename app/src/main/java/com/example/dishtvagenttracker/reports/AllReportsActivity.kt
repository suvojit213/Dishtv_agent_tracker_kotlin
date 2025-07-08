package com.example.dishtvagenttracker.reports

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.utils.ExcelGenerator
import com.example.dishtvagenttracker.utils.PdfGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllReportsActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE = 101
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_reports)

        database = AppDatabase.getDatabase(applicationContext)

        val generateExcelButton: Button = findViewById(R.id.generateExcelButton)
        val generatePdfButton: Button = findViewById(R.id.generatePdfButton)

        generateExcelButton.setOnClickListener {
            checkPermissionAndGenerateReport(ReportType.EXCEL)
        }

        generatePdfButton.setOnClickListener {
            checkPermissionAndGenerateReport(ReportType.PDF)
        }
    }

    private fun checkPermissionAndGenerateReport(reportType: ReportType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                generateReport(reportType)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }
        } else {
            generateReport(reportType)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, try to generate report again
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
                // Note: We don't know which report type was requested, so user needs to click again
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateReport(reportType: ReportType) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dailyEntries = database.dailyEntryDao().getAllDailyEntries()
                if (dailyEntries.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AllReportsActivity, "No entries to generate report.", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val file = when (reportType) {
                    ReportType.EXCEL -> ExcelGenerator.generateDailyEntriesCsv(applicationContext, dailyEntries)
                    ReportType.PDF -> PdfGenerator.generateDailyEntriesPdf(applicationContext, dailyEntries)
                }

                withContext(Dispatchers.Main) {
                    if (file != null) {
                        Toast.makeText(this@AllReportsActivity, "Report saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@AllReportsActivity, "Failed to generate report.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllReportsActivity, "Error generating report: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    enum class ReportType { EXCEL, PDF }
}