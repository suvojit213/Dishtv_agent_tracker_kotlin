package com.example.dishtvagenttracker.theme_selection

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.dishtvagenttracker.R

class ThemeSelectionActivity : AppCompatActivity() {

    private lateinit var themeRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_selection)

        themeRadioGroup = findViewById(R.id.themeRadioGroup)

        // Load saved theme preference
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val currentTheme = sharedPref.getInt("selected_theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        when (currentTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> findViewById<RadioButton>(R.id.lightThemeRadioButton).isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> findViewById<RadioButton>(R.id.darkThemeRadioButton).isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> findViewById<RadioButton>(R.id.systemDefaultThemeRadioButton).isChecked = true
        }

        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedTheme = when (checkedId) {
                R.id.lightThemeRadioButton -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.darkThemeRadioButton -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.systemDefaultThemeRadioButton -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            saveThemePreference(selectedTheme)
            AppCompatDelegate.setDefaultNightMode(selectedTheme)
        }
    }

    private fun saveThemePreference(theme: Int) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("selected_theme", theme)
            apply()
        }
    }
}