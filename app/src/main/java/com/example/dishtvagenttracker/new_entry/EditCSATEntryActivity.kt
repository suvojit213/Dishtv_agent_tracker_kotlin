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
import java.util.Date
import java.util.Locale

class EditCSATEntryActivity : AppCompatActivity() {

    private lateinit var datePickerButton: Button
    private lateinit var t2CountEditText: EditText
    private lateinit var b2CountEditText: EditText
    private lateinit var nCountEditText: EditText
    private lateinit var updateCSATEntryButton: Button
    private lateinit var deleteCSATEntryButton: Button

    private val calendar = Calendar.getInstance()
    private lateinit var database: AppDatabase
    private var csatEntryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_csat_entry)

        database = AppDatabase.getDatabase(applicationContext)

        datePickerButton = findViewById(R.id.datePickerButton)
        t2CountEditText = findViewById(R.id.t2CountEditText)
        b2CountEditText = findViewById(R.id.b2CountEditText)
        nCountEditText = findViewById(R.id.nCountEditText)
        updateCSATEntryButton = findViewById(R.id.updateCSATEntryButton)
        deleteCSATEntryButton = findViewById(R.id.deleteCSATEntryButton)

        csatEntryId = intent.getIntExtra("csat_entry_id", -1).takeIf { it != -1 }
        val dateMillis = intent.getLongExtra("csat_entry_date", -1L).takeIf { it != -1L }
        val t2Count = intent.getIntExtra("csat_entry_t2_count", 0)
        val b2Count = intent.getIntExtra("csat_entry_b2_count", 0)
        val nCount = intent.getIntExtra("csat_entry_n_count", 0)

        if (dateMillis != null) {
            calendar.time = Date(dateMillis)
            updateDateButtonText()
        }
        t2CountEditText.setText(t2Count.toString())
        b2CountEditText.setText(b2Count.toString())
        nCountEditText.setText(nCount.toString())

        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        updateCSATEntryButton.setOnClickListener {
            updateCSATEntry()
        }

        deleteCSATEntryButton.setOnClickListener {
            deleteCSATEntry()
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

    private fun updateCSATEntry() {
        val id = csatEntryId
        val date = calendar.time
        val t2Count = t2CountEditText.text.toString().toIntOrNull() ?: 0
        val b2Count = b2CountEditText.text.toString().toIntOrNull() ?: 0
        val nCount = nCountEditText.text.toString().toIntOrNull() ?: 0

        if (id == null) {
            Toast.makeText(this, "Error: CSAT Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedEntry = CSATEntry(
            id = id,
            date = date,
            t2Count = t2Count,
            b2Count = b2Count,
            nCount = nCount
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.csatEntryDao().updateCSATEntry(updatedEntry)
                runOnUiThread {
                    Toast.makeText(this@EditCSATEntryActivity, "CSAT Entry updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditCSATEntryActivity, "Error updating CSAT entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteCSATEntry() {
        val id = csatEntryId
        if (id == null) {
            Toast.makeText(this, "Error: CSAT Entry ID not found.", Toast.LENGTH_SHORT).show()
            return
        }

        val entryToDelete = CSATEntry(
            id = id,
            date = Date(), // Date and other fields don't matter for deletion by ID
            t2Count = 0,
            b2Count = 0,
            nCount = 0
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.csatEntryDao().deleteCSATEntry(entryToDelete)
                runOnUiThread {
                    Toast.makeText(this@EditCSATEntryActivity, "CSAT Entry deleted successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditCSATEntryActivity, "Error deleting CSAT entry: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}