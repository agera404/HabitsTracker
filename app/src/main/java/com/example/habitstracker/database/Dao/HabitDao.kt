package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.Habit

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit): Long

    @Update
    abstract suspend fun update(habit: Habit)

    @Delete
    abstract fun delete(habit: Habit)

    @Query("SELECT * FROM habits WHERE id_habit in(:id)")
    abstract fun findHabitById(id: Long): Habit?

}