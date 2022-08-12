package com.example.habitstracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events")
class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "id_event") val id_event: Long?,
    @ColumnInfo(name = "id_habit") val id_habit: Long,
    @ColumnInfo(name = "id_calendar") val id_calendar: Int
) {
}