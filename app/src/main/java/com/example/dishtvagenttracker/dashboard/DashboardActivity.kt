package com.example.dishtvagenttracker.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.add_entry.AddEntryActivity
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.DailyEntry
import com.example.dishtvagenttracker.dashboard.adapter.DailyEntryAdapter
import com.example.dishtvagenttracker.goals.SetGoalsActivity
import com.example.dishtvagenttracker.reports.AllReportsActivity
import com.example.dishtvagenttracker.settings.SettingsActivity
import com.example.dishtvagenttracker.utils.GoalManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {

    private lateinit var addEntryButton: Button
    private lateinit var monthlyPerformanceButton: Button
    private lateinit var allReportsButton: Button
    private lateinit var settingsButton: Button
    private lateinit var setGoalsButton: Button
    private lateinit var dailyEntriesRecyclerView: RecyclerView
    private lateinit var dailyEntryAdapter: DailyEntryAdapter
    private lateinit var database: AppDatabase
    private lateinit var goalManager: GoalManager
    private lateinit var goalHoursTextView: TextView
    private lateinit var goalCallsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        database = AppDatabase.getDatabase(applicationContext)
        goalManager = GoalManager(applicationContext)

        addEntryButton = findViewById(R.id.addEntryButton)
        monthlyPerformanceButton = findViewById(R.id.monthlyPerformanceButton)
        allReportsButton = findViewById(R.id.allReportsButton)
        settingsButton = findViewById(R.id.settingsButton)
        setGoalsButton = findViewById(R.id.setGoalsButton)
        val viewCSATSummaryButton: Button = findViewById(R.id.viewCSATSummaryButton)
        val viewCQSummaryButton: Button = findViewById(R.id.viewCQSummaryButton)
        dailyEntriesRecyclerView = findViewById(R.id.dailyEntriesRecyclerView)
        goalHoursTextView = findViewById(R.id.goalHoursTextView)
        goalCallsTextView = findViewById(R.id.goalCallsTextView)

        dailyEntriesRecyclerView.layoutManager = LinearLayoutManager(this)

        addEntryButton.setOnClickListener {
            val intent = Intent(this, NewEntrySelectionActivity::class.java)
            startActivity(intent)
        }

        monthlyPerformanceButton.setOnClickListener {
            val intent = Intent(this, MonthlyPerformanceActivity::class.java)
            startActivity(intent)
        }

        allReportsButton.setOnClickListener {
            val intent = Intent(this, AllReportsActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        setGoalsButton.setOnClickListener {
            val intent = Intent(this, SetGoalsActivity::class.java)
            startActivity(intent)
        }

        viewCSATSummaryButton.setOnClickListener {
            val intent = Intent(this, MonthlyCSATActivity::class.java)
            startActivity(intent)
        }

        viewCQSummaryButton.setOnClickListener {
            val intent = Intent(this, MonthlyCQActivity::class.java)
            startActivity(intent)
        }

        val viewAllCSATEntriesButton: Button = findViewById(R.id.viewAllCSATEntriesButton)
        viewAllCSATEntriesButton.setOnClickListener {
            val intent = Intent(this, AllCSATEntriesActivity::class.java)
            startActivity(intent)
        }

    override fun onResume() {
        super.onResume()
        loadDailyEntries()
        updateGoalDisplay()
    }

    private fun loadDailyEntries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dailyEntries = database.dailyEntryDao().getAllDailyEntries()
                withContext(Dispatchers.Main) {
                    dailyEntryAdapter = DailyEntryAdapter(dailyEntries) { dailyEntry ->
                        val intent = Intent(this@DashboardActivity, EditDailyEntryActivity::class.java).apply {
                            putExtra("daily_entry_id", dailyEntry.id)
                            putExtra("daily_entry_date", dailyEntry.date.time)
                            putExtra("daily_entry_login_hours", dailyEntry.loginHours)
                            putExtra("daily_entry_login_minutes", dailyEntry.loginMinutes)
                            putExtra("daily_entry_login_seconds", dailyEntry.loginSeconds)
                            putExtra("daily_entry_call_count", dailyEntry.callCount)
                        }
                        startActivity(intent)
                    }
                    dailyEntriesRecyclerView.adapter = dailyEntryAdapter
                    if (dailyEntries.isEmpty()) {
                        Toast.makeText(this@DashboardActivity, "No entries found. Add one!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DashboardActivity, "Error loading entries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateGoalDisplay() {
        val goalHours = goalManager.getGoalHours()
        val goalCalls = goalManager.getGoalCalls()
        goalHoursTextView.text = "Goal Hours: $goalHours"
        goalCallsTextView.text = "Goal Calls: $goalCalls"
    }
}