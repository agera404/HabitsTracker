package com.example.habitstracker.database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.habitstracker.models.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insert(entity: EventEntity): Long

    @Update
    suspend fun update(entity: EventEntity)

    @Delete
    fun delete(entity: EventEntity)
}