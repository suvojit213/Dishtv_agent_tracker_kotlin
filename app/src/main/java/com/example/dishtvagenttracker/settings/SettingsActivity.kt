package com.example.dishtvagenttracker.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.app_info.AppInfoActivity
import com.example.dishtvagenttracker.theme_selection.ThemeSelectionActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSelectionButton: Button = findViewById(R.id.themeSelectionButton)
        themeSelectionButton.setOnClickListener {
            val intent = Intent(this, ThemeSelectionActivity::class.java)
            startActivity(intent)
        }

        val appInfoButton: Button = findViewById(R.id.appInfoButton)
        appInfoButton.setOnClickListener {
            val intent = Intent(this, AppInfoActivity::class.java)
            startActivity(intent)
        }
    }
}