package com.example.habitstracker.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notification", indices = [Index(value = arrayOf("id_habit"), unique = true)])
class NotificationEntity(
    @PrimaryKey val id_notification: Int?,
    val id_habit: Long,
    var time: Long
) {
}