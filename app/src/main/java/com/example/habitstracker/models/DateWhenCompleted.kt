package com.example.habitstracker.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(ForeignKey
        (entity = Habit::class,
        parentColumns = arrayOf("id_habit"),
        childColumns = arrayOf("habit"))))
class DateWhenCompleted(
    @PrimaryKey var id_date: Long? = null,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "habit") var habit: Long
) {
}