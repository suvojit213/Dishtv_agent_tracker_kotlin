package com.example.dishtvagenttracker.new_entry

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.data.model.CQEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCQEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var percentageEditText: EditText
    private lateinit var saveCQEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cq_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        percentageEditText = findViewById(R.id.percentageEditText)
        saveCQEntryButton = findViewById(R.id.saveCQEntryButton)

        updateDateButtonText()

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        saveCQEntryButton.setOnClickListener {
            saveCQEntry()
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

    private fun saveCQEntry() {
        val auditDate = calendar.time
        val percentage = percentageEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (percentage == 0.0) {
            Toast.makeText(this, "Please enter a valid percentage", Toast.LENGTH_SHORT).show()
            return
        }

        val cqEntry = CQEntry(
            auditDate = auditDate,
            percentage = percentage
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.cqEntryDao().insertCQEntry(cqEntry)
                runOnUiThread {
                    Toast.makeText(this@AddCQEntryActivity, "CQ Entry saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AddCQEntryActivity, "Error saving CQ entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}