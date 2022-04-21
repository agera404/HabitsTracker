package com.example.habitstracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "habits", indices = [Index(value = arrayOf("name"), unique = true)])
class Habit(@PrimaryKey(autoGenerate = true) val id_habit: Long? = null,
            @ColumnInfo(name = "name") var name: String,
            ) {

}