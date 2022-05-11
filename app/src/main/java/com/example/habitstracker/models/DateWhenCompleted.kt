package com.example.habitstracker.models

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.text.SimpleDateFormat

import java.util.*

@Entity(
    foreignKeys = arrayOf(ForeignKey
        (entity = Habit::class,
        parentColumns = arrayOf("id_habit"),
        childColumns = arrayOf("id_habit"),
        onDelete = CASCADE)),
        indices = [Index(value = arrayOf("date","id_habit"), unique = true)])
class DateWhenCompleted(
    @PrimaryKey var id_date: Long? = null,
    @ColumnInfo(name = "date") @TypeConverters(DateConverter::class) var date: Date,
    @ColumnInfo(name = "id_habit") var id_habit: Long
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
    fun convertDateToLong(date:Date): Long{
        return Calendar.getInstance().apply { time = date }.timeInMillis
    }
}