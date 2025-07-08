package com.example.dishtvagenttracker.csat_cq_summary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.csat_cq_summary.adapter.CQSummaryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonthlyCQActivity : AppCompatActivity() {

    private lateinit var cqSummariesRecyclerView: RecyclerView
    private lateinit var cqSummaryAdapter: CQSummaryAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_cq)

        database = AppDatabase.getDatabase(applicationContext)

        cqSummariesRecyclerView = findViewById(R.id.cqSummariesRecyclerView)
        cqSummariesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadMonthlyCQSummaries()
    }

    private fun loadMonthlyCQSummaries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cqSummaries = database.cqEntryDao().getMonthlyCQSummaries()
                withContext(Dispatchers.Main) {
                    cqSummaryAdapter = CQSummaryAdapter(cqSummaries)
                    cqSummariesRecyclerView.adapter = cqSummaryAdapter
                    if (cqSummaries.isEmpty()) {
                        Toast.makeText(this@MonthlyCQActivity, "No monthly CQ summaries found.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MonthlyCQActivity, "Error loading monthly CQ summaries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}