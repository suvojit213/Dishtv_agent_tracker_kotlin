package com.example.dishtvagenttracker.new_entry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.dishtvagenttracker.R

class NewEntrySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry_selection)

        val addDailyEntryButton: Button = findViewById(R.id.addDailyEntryButton)
        val addCSATEntryButton: Button = findViewById(R.id.addCSATEntryButton)
        val addCQEntryButton: Button = findViewById(R.id.addCQEntryButton)

        addDailyEntryButton.setOnClickListener {
            val intent = Intent(this, AddDailyEntryActivity::class.java)
            startActivity(intent)
        }

        addCSATEntryButton.setOnClickListener {
            val intent = Intent(this, AddCSATEntryActivity::class.java)
            startActivity(intent)
        }

        addCQEntryButton.setOnClickListener {
            val intent = Intent(this, AddCQEntryActivity::class.java)
            startActivity(intent)
        }
    }
}