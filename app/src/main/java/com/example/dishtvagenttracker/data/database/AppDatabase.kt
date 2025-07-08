package com.example.dishtvagenttracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dishtvagenttracker.data.dao.DailyEntryDao
import com.example.dishtvagenttracker.data.model.DailyEntry

@Database(entities = [DailyEntry::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dailyEntryDao(): DailyEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dishtv_agent_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}