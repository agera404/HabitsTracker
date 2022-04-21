package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.Habit

@Dao
interface HabitDao {
    @Insert
    suspend fun Insert(habit: Habit): Long

    @Update
    abstract suspend fun Update(habit: Habit)

    @Delete
    abstract fun Delete(habit: Habit)

}