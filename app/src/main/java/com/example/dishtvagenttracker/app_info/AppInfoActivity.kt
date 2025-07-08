package com.example.dishtvagenttracker.app_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.dishtvagenttracker.R

class AppInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        val appNameTextView: TextView = findViewById(R.id.appNameTextView)
        val appVersionTextView: TextView = findViewById(R.id.appVersionTextView)

        appNameTextView.text = getString(R.string.app_name)
        appVersionTextView.text = "Version: ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }
}