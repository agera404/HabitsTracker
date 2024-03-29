package com.example.habitstracker.data.Dao

import androidx.room.*
import com.example.habitstracker.models.HabitEntity

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(habitEntity: HabitEntity): Long?

    @Update
    suspend fun update(habitEntity: HabitEntity)

    @Delete
    fun delete(habitEntity: HabitEntity)

    @Query("SELECT * FROM habits WHERE id_habit in(:id)")
    fun findHabitById(id: Long): HabitEntity?

}