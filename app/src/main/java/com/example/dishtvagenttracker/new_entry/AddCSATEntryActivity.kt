package com.example.dishtvagenttracker.new_entry

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.CSATEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCSATEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var t2CountEditText: EditText
    private lateinit var b2CountEditText: EditText
    private lateinit var nCountEditText: EditText
    private lateinit var saveCSATEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_csat_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        t2CountEditText = findViewById(R.id.t2CountEditText)
        b2CountEditText = findViewById(R.id.b2CountEditText)
        nCountEditText = findViewById(R.id.nCountEditText)
        saveCSATEntryButton = findViewById(R.id.saveCSATEntryButton)

        updateDateButtonText()

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        saveCSATEntryButton.setOnClickListener {
            saveCSATEntry()
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

    private fun saveCSATEntry() {
        val date = calendar.time
        val t2Count = t2CountEditText.text.toString().toIntOrNull() ?: 0
        val b2Count = b2CountEditText.text.toString().toIntOrNull() ?: 0
        val nCount = nCountEditText.text.toString().toIntOrNull() ?: 0

        if (t2Count == 0 && b2Count == 0 && nCount == 0) {
            Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
            return
        }

        val csatEntry = CSATEntry(
            date = date,
            t2Count = t2Count,
            b2Count = b2Count,
            nCount = nCount
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.csatEntryDao().insertCSATEntry(csatEntry)
                runOnUiThread {
                    Toast.makeText(this@AddCSATEntryActivity, "CSAT Entry saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AddCSATEntryActivity, "Error saving CSAT entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}