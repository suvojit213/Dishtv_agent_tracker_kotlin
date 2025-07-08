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
import java.util.Date
import java.util.Locale

class EditCQEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var percentageEditText: EditText
    private lateinit var updateCQEntryButton: Button
    private lateinit var deleteCQEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase
    private var cqEntryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cq_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        percentageEditText = findViewById(R.id.percentageEditText)
        updateCQEntryButton = findViewById(R.id.updateCQEntryButton)
        deleteCQEntryButton = findViewById(R.id.deleteCQEntryButton)

        cqEntryId = intent.getIntExtra("cq_entry_id", -1).takeIf { it != -1 }
        val auditDateMillis = intent.getLongExtra("cq_entry_audit_date", -1L).takeIf { it != -1L }
        val percentage = intent.getDoubleExtra("cq_entry_percentage", 0.0)

        if (auditDateMillis != null) {
            calendar.time = Date(auditDateMillis)
            updateDateButtonText()
        }
        percentageEditText.setText(percentage.toString())

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        updateCQEntryButton.setOnClickListener {
            updateCQEntry()
        }

        deleteCQEntryButton.setOnClickListener {
            deleteCQEntry()
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

    private fun updateCQEntry() {
        val id = cqEntryId
        val auditDate = calendar.time
        val percentage = percentageEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (id == null) {
            Toast.makeText(this, "Error: CQ Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedEntry = CQEntry(
            id = id,
            auditDate = auditDate,
            percentage = percentage
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.cqEntryDao().updateCQEntry(updatedEntry)
                runOnUiThread {
                    Toast.makeText(this@EditCQEntryActivity, "CQ Entry updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditCQEntryActivity, "Error updating CQ entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteCQEntry() {
        val id = cqEntryId
        if (id == null) {
            Toast.makeText(this, "Error: CQ Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val entryToDelete = CQEntry(
            id = id,
            auditDate = Date(), // Date and other fields don't matter for deletion by ID
            percentage = 0.0
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.cqEntryDao().deleteCQEntry(entryToDelete)
                runOnUiThread {
                    Toast.makeText(this@EditCQEntryActivity, "CQ Entry deleted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditCQEntryActivity, "Error deleting CQ entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}