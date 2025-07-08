package com.example.dishtvagenttracker.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dishtvagenttracker.dashboard.DashboardActivity

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        markOnboardingComplete()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun markOnboardingComplete() {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean("onboarding_complete", true)
            apply()
        }
    }
}