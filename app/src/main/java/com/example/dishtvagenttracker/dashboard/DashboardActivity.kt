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
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.DailyEntry
import com.example.dishtvagenttracker.dashboard.adapter.DailyEntryAdapter
import com.example.dishtvagenttracker.utils.GoalManager
import com.example.dishtvagenttracker.new_entry.AddDailyEntryActivity
import com.example.dishtvagenttracker.new_entry.EditDailyEntryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {

    private lateinit var addEntryButton: Button
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
        dailyEntriesRecyclerView = findViewById(R.id.dailyEntriesRecyclerView)
        goalHoursTextView = findViewById(R.id.goalHoursTextView)
        goalCallsTextView = findViewById(R.id.goalCallsTextView)

        dailyEntriesRecyclerView.layoutManager = LinearLayoutManager(this)

        addEntryButton.setOnClickListener {
            val intent = Intent(this, AddDailyEntryActivity::class.java)
            startActivity(intent)
        }
    } // End of onCreate

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