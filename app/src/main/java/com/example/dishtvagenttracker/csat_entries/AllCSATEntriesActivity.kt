package com.example.dishtvagenttracker.csat_entries

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.data.database.AppDatabase
import com.example.dishtvagenttracker.new_entry.EditCSATEntryActivity
import com.example.dishtvagenttracker.data.dao.CSATEntryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllCSATEntriesActivity : AppCompatActivity() {

    private lateinit var csatEntriesRecyclerView: RecyclerView
    private lateinit var csatEntryListAdapter: CSATEntryListAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_csat_entries)

        database = AppDatabase.getDatabase(applicationContext)

        csatEntriesRecyclerView = findViewById(R.id.csatEntriesRecyclerView)
        csatEntriesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadCSATEntries()
    }

    private fun loadCSATEntries() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val csatEntries = database.csatEntryDao().getAllCSATEntries()
                withContext(Dispatchers.Main) {
                    csatEntryListAdapter = CSATEntryListAdapter(csatEntries) { csatEntry ->
                        val intent = Intent(this@AllCSATEntriesActivity, EditCSATEntryActivity::class.java).apply {
                            putExtra("csat_entry_id", csatEntry.id)
                            putExtra("csat_entry_date", csatEntry.date.time)
                            putExtra("csat_entry_t2_count", csatEntry.t2Count)
                            putExtra("csat_entry_b2_count", csatEntry.b2Count)
                            putExtra("csat_entry_n_count", csatEntry.nCount)
                        }
                        startActivity(intent)
                    }
                    csatEntriesRecyclerView.adapter = csatEntryListAdapter
                    if (csatEntries.isEmpty()) {
                        Toast.makeText(this@AllCSATEntriesActivity, "No CSAT entries found. Add one!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AllCSATEntriesActivity, "Error loading CSAT entries: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}