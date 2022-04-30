package com.example.habitstracker.database

import android.content.Context
import androidx.room.*
import androidx.room.Database
import com.example.habitstracker.database.Dao.DateDao
import com.example.habitstracker.database.Dao.HabitDao
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit


@Database(entities = [Habit::class, DateWhenCompleted::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun dateDao(): DateDao

    companion object {


        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context,
                AppDatabase::class.java, "habits.db").allowMainThreadQueries().build()
        }
    }
}

