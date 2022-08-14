package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insert(entity: EventEntity): Long

    @Update
    suspend fun update(entity: EventEntity)

    @Delete
    fun delete(entity: EventEntity)

    @Query("SELECT * FROM events WHERE id_event = :id")
    fun getEventById(id: Long): EventEntity

    @Query("SELECT * FROM events WHERE id_calendar = :idCalendar AND id_habit = :idHabit AND date = :date")
    fun getEventByHabitIdCalendarIdAndDate(idCalendar: Int?, idHabit: Long, date: String): EventEntity
}