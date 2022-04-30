package com.example.habitstracker.models

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.text.SimpleDateFormat

import java.util.*

@Entity(
    foreignKeys = arrayOf(ForeignKey
        (entity = Habit::class,
        parentColumns = arrayOf("id_habit"),
        childColumns = arrayOf("habit"),
        onDelete = CASCADE)),
        indices = [Index(value = arrayOf("date","habit"), unique = true)])
class DateWhenCompleted(
    @PrimaryKey var id_date: Long? = null,
    @ColumnInfo(name = "date") @TypeConverters(DateConverter::class) var date: Date,
    @ColumnInfo(name = "habit") var habit: Long
) {
}

object DateConverter{
    @TypeConverter
    @JvmStatic
    fun dateToString(date: Date): String?{
        return SimpleDateFormat("dd/M/yyyy").format(date)
    }
    @TypeConverter
    @JvmStatic
    fun stringToDate(string: String): Date?{
        return SimpleDateFormat("dd/M/yyyy").parse(string)
    }
}