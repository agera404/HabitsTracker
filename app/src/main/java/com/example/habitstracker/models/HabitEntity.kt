package com.example.habitstracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "habits", indices = [Index(value = ["name"], unique = true)])
class HabitEntity(@PrimaryKey val id_habit: Long? = null,
                  @ColumnInfo(name = "name") var name: String,
                  @ColumnInfo(name = "calendar_id") var calendar_id: Int? = null
            ) {

}