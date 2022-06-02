package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.NotificationEntity

@Dao
interface NotificationDao {
    @Insert
    suspend fun insert(entity: NotificationEntity): Long

    @Update
    suspend fun update(entity: NotificationEntity)

    @Delete
    fun delete(entity: NotificationEntity)

    @Query("SELECT * FROM notification WHERE id_habit in(:id_habit)")
    fun getEntityByHabitID(id_habit: Long): NotificationEntity?

}