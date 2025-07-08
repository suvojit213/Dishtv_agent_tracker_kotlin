package com.example.dishtvagenttracker.new_entry

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.DailyEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditDailyEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var loginHoursEditText: EditText
    private lateinit var loginMinutesEditText: EditText
    private lateinit var loginSecondsEditText: EditText
    private lateinit var callCountEditText: EditText
    private lateinit var updateEntryButton: Button
    private lateinit var deleteEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase
    private var dailyEntryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_daily_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        loginHoursEditText = findViewById(R.id.loginHoursEditText)
        loginMinutesEditText = findViewById(R.id.loginMinutesEditText)
        loginSecondsEditText = findViewById(R.id.loginSecondsEditText)
        callCountEditText = findViewById(R.id.callCountEditText)
        updateEntryButton = findViewById(R.id.updateEntryButton)
        deleteEntryButton = findViewById(R.id.deleteEntryButton)

        dailyEntryId = intent.getIntExtra("daily_entry_id", -1).takeIf { it != -1 }
        val dateMillis = intent.getLongExtra("daily_entry_date", -1L).takeIf { it != -1L }
        val loginHours = intent.getIntExtra("daily_entry_login_hours", 0)
        val loginMinutes = intent.getIntExtra("daily_entry_login_minutes", 0)
        val loginSeconds = intent.getIntExtra("daily_entry_login_seconds", 0)
        val callCount = intent.getIntExtra("daily_entry_call_count", 0)

        if (dateMillis != null) {
            calendar.time = Date(dateMillis)
            updateDateButtonText()
        }
        loginHoursEditText.setText(loginHours.toString())
        loginMinutesEditText.setText(loginMinutes.toString())
        loginSecondsEditText.setText(loginSeconds.toString())
        callCountEditText.setText(callCount.toString())

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        updateEntryButton.setOnClickListener {
            updateEntry()
        }

        deleteEntryButton.setOnClickListener {
            deleteEntry()
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                updateDateButtonText()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun updateDateButtonText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        datePickerButton.text = dateFormat.format(calendar.time)
    }

    private fun updateEntry() {
        val id = dailyEntryId
        val date = calendar.time
        val loginHours = loginHoursEditText.text.toString().toIntOrNull() ?: 0
        val loginMinutes = loginMinutesEditText.text.toString().toIntOrNull() ?: 0
        val loginSeconds = loginSecondsEditText.text.toString().toIntOrNull() ?: 0
        val callCount = callCountEditText.text.toString().toIntOrNull() ?: 0

        if (id == null) {
            Toast.makeText(this, "Error: Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedEntry = DailyEntry(
            id = id,
            date = date,
            loginHours = loginHours,
            loginMinutes = loginMinutes,
            loginSeconds = loginSeconds,
            callCount = callCount
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.dailyEntryDao().updateDailyEntry(updatedEntry)
                runOnUiThread {
                    Toast.makeText(this@EditDailyEntryActivity, "Entry updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditDailyEntryActivity, "Error updating entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteEntry() {
        val id = dailyEntryId
        if (id == null) {
            Toast.makeText(this, "Error: Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val entryToDelete = DailyEntry(
            id = id,
            date = Date(), // Date and other fields don't matter for deletion by ID
            loginHours = 0,
            loginMinutes = 0,
            loginSeconds = 0,
            callCount = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.dailyEntryDao().deleteDailyEntry(entryToDelete)
                runOnUiThread {
                    Toast.makeText(this@EditDailyEntryActivity, "Entry deleted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditDailyEntryActivity, "Error deleting entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}