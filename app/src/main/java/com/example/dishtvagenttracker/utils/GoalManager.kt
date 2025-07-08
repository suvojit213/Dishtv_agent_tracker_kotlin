package com.example.dishtvagenttracker.utils

import android.content.Context
import android.content.SharedPreferences

class GoalManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("goal_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GOAL_HOURS = "goal_hours"
        private const val KEY_GOAL_CALLS = "goal_calls"
        private const val DEFAULT_GOAL_HOURS = 150
        private const val DEFAULT_GOAL_CALLS = 1000
    }

    fun saveGoals(hours: Int, calls: Int) {
        with(sharedPreferences.edit()) {
            putInt(KEY_GOAL_HOURS, hours)
            putInt(KEY_GOAL_CALLS, calls)
            apply()
        }
    }

    fun getGoalHours(): Int {
        return sharedPreferences.getInt(KEY_GOAL_HOURS, DEFAULT_GOAL_HOURS)
    }

    fun getGoalCalls(): Int {
        return sharedPreferences.getInt(KEY_GOAL_CALLS, DEFAULT_GOAL_CALLS)
    }
}