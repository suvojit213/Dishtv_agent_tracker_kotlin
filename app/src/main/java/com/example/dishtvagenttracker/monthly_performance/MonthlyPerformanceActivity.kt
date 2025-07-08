package com.example.dishtvagenttracker.monthly_performance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.MonthlySummary
import com.example.dishtvagenttracker.monthly_performance.adapter.MonthlySummaryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyPerformanceActivity : AppCompatActivity() {

    private lateinit var monthlySummariesRecyclerView: RecyclerView
    private lateinit var monthlySummaryAdapter: MonthlySummaryAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_performance)

        database = AppDatabase.getDatabase(applicationContext)

        monthlySummariesRecyclerView = findViewById(R.id.monthlySummariesRecyclerView)
        monthlySummariesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadMonthlySummaries()
    }

    private fun loadMonthlySummaries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dailySummariesRaw = database.dailyEntryDao().getMonthlySummaries()
                val csatSummaries = database.csatEntryDao().getMonthlyCSATSummaries()

                val monthlySummaries = dailySummariesRaw.map { dailySummary ->
                    val matchingCSATSummary = csatSummaries.find {
                        it.month == dailySummary.month && it.year == dailySummary.year
                    }
                    MonthlySummary(
                        month = dailySummary.month,
                        year = dailySummary.year,
                        totalLoginHours = dailySummary.totalLoginHours,
                        totalCalls = dailySummary.totalCalls,
                        csatSummary = matchingCSATSummary
                    )
                }

                withContext(Dispatchers.Main) {
                    monthlySummaryAdapter = MonthlySummaryAdapter(monthlySummaries)
                    monthlySummariesRecyclerView.adapter = monthlySummaryAdapter
                    if (monthlySummaries.isEmpty()) {
                        Toast.makeText(this@MonthlyPerformanceActivity, "No monthly summaries found.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MonthlyPerformanceActivity, "Error loading monthly summaries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}