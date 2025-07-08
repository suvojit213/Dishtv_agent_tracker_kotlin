package com.example.dishtvagenttracker.goals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dishtvagenttracker.R
import com.example.dishtvagenttracker.utils.GoalManager

class SetGoalsActivity : AppCompatActivity() {

    private lateinit var goalHoursEditText: EditText
    private lateinit var goalCallsEditText: EditText
    private lateinit var saveGoalsButton: Button
    private lateinit var goalManager: GoalManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        goalManager = GoalManager(this)

        goalHoursEditText = findViewById(R.id.goalHoursEditText)
        goalCallsEditText = findViewById(R.id.goalCallsEditText)
        saveGoalsButton = findViewById(R.id.saveGoalsButton)

        // Load existing goals
        goalHoursEditText.setText(goalManager.getGoalHours().toString())
        goalCallsEditText.setText(goalManager.getGoalCalls().toString())

        saveGoalsButton.setOnClickListener {
            saveGoals()
        }
    }

    private fun saveGoals() {
        val hours = goalHoursEditText.text.toString().toIntOrNull()
        val calls = goalCallsEditText.text.toString().toIntOrNull()

        if (hours != null && calls != null) {
            goalManager.saveGoals(hours, calls)
            Toast.makeText(this, "Goals saved successfully!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please enter valid numbers for goals.", Toast.LENGTH_SHORT).show()
        }
    }
}