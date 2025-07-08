package com.example.dishtvagenttracker.csat_cq_summary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.csat_cq_summary.adapter.CSATSummaryAdapter
import com.example.dishtvagenttracker.data.dao.CSATEntryDao

class MonthlyCSATActivity : AppCompatActivity() {

    private lateinit var csatSummariesRecyclerView: RecyclerView
    private lateinit var csatSummaryAdapter: CSATSummaryAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_csat)

        database = AppDatabase.getDatabase(applicationContext)

        csatSummariesRecyclerView = findViewById(R.id.csatSummariesRecyclerView)
        csatSummariesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadMonthlyCSATSummaries()
    }

    private fun loadMonthlyCSATSummaries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val csatSummaries = database.csatEntryDao().getMonthlyCSATSummaries()
                withContext(Dispatchers.Main) {
                    csatSummaryAdapter = CSATSummaryAdapter(csatSummaries) { /* No-op for now, as onEditClick is not implemented */ }
                    csatSummariesRecyclerView.adapter = csatSummaryAdapter
                    if (csatSummaries.isEmpty()) {
                        Toast.makeText(this@MonthlyCSATActivity, "No monthly CSAT summaries found.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MonthlyCSATActivity, "Error loading monthly CSAT summaries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}