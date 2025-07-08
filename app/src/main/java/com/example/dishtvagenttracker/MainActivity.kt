package com.example.dishtvagenttracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.dishtvagenttracker.dashboard.DashboardActivity
import com.example.dishtvagenttracker.onboarding.OnboardingActivity

import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val onboardingComplete = sharedPref.getBoolean("onboarding_complete", false)
        val savedTheme = sharedPref.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedTheme)

        Handler(Looper.getMainLooper()).postDelayed({
            if (onboardingComplete) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000) // 2 second delay
    }
}