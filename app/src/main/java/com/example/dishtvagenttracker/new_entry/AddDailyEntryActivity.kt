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
import java.util.Locale

class AddDailyEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var loginHoursEditText: EditText
    private lateinit var loginMinutesEditText: EditText
    private lateinit var loginSecondsEditText: EditText
    private lateinit var callCountEditText: EditText
    private lateinit var saveEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_daily_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        loginHoursEditText = findViewById(R.id.loginHoursEditText)
        loginMinutesEditText = findViewById(R.id.loginMinutesEditText)
        loginSecondsEditText = findViewById(R.id.loginSecondsEditText)
        callCountEditText = findViewById(R.id.callCountEditText)
        saveEntryButton = findViewById(R.id.saveEntryButton)

        updateDateButtonText()

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        saveEntryButton.setOnClickListener {
            saveEntry()
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

    private fun saveEntry() {
        val date = calendar.time
        val loginHours = loginHoursEditText.text.toString().toIntOrNull() ?: 0
        val loginMinutes = loginMinutesEditText.text.toString().toIntOrNull() ?: 0
        val loginSeconds = loginSecondsEditText.text.toString().toIntOrNull() ?: 0
        val callCount = callCountEditText.text.toString().toIntOrNull() ?: 0

        if (loginHours == 0 && loginMinutes == 0 && loginSeconds == 0 && callCount == 0) {
            Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
            return
        }

        val dailyEntry = DailyEntry(
            date = date,
            loginHours = loginHours,
            loginMinutes = loginMinutes,
            loginSeconds = loginSeconds,
            callCount = callCount
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.dailyEntryDao().insertDailyEntry(dailyEntry)
                runOnUiThread {
                    Toast.makeText(this@AddDailyEntryActivity, "Entry saved successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after saving
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AddDailyEntryActivity, "Error saving entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}