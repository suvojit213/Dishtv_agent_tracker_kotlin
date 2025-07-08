package com.example.dishtvagenttracker.cq_entries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.new_entry.EditCQEntryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCQEntriesActivity : AppCompatActivity() {

    private lateinit var cqEntriesRecyclerView: RecyclerView
    private lateinit var cqEntryListAdapter: CQEntryListAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_cq_entries)

        database = AppDatabase.getDatabase(applicationContext)

        cqEntriesRecyclerView = findViewById(R.id.cqEntriesRecyclerView)
        cqEntriesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadCQEntries()
    }

    private fun loadCQEntries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cqEntries = database.cqEntryDao().getAllCQEntries()
                withContext(Dispatchers.Main) {
                    cqEntryListAdapter = CQEntryListAdapter(cqEntries) { cqEntry ->
                        val intent = Intent(this@AllCQEntriesActivity, EditCQEntryActivity::class.java).apply {
                            putExtra("cq_entry_id", cqEntry.id)
                            putExtra("cq_entry_audit_date", cqEntry.auditDate.time)
                            putExtra("cq_entry_percentage", cqEntry.percentage)
                        }
                        startActivity(intent)
                    }
                    cqEntriesRecyclerView.adapter = cqEntryListAdapter
                    if (cqEntries.isEmpty()) {
                        Toast.makeText(this@AllCQEntriesActivity, "No CQ entries found. Add one!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllCQEntriesActivity, "Error loading CQ entries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}