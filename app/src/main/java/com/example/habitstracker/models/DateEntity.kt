package com.example.habitstracker.models

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(
    foreignKeys = arrayOf(ForeignKey
        (entity = HabitEntity::class,
        parentColumns = arrayOf("id_habit"),
        childColumns = arrayOf("id_habit"),
        onDelete = CASCADE)),
        indices = [Index(value = arrayOf("date","id_habit"), unique = true)])
class DateEntity(
    @PrimaryKey var id_date: Long? = null,
    @ColumnInfo(name = "date") @TypeConverters(DateConverter::class) var date: LocalDate,
    @ColumnInfo(name = "id_habit") var id_habit: Long
) {
}

object DateConverter{
    @TypeConverter
    @JvmStatic
    fun toString(date: LocalDate): String?{
        return DateTimeFormatter.ofPattern("dd/M/yyyy").format(date)
    }
    @TypeConverter
    @JvmStatic
    fun toDate(string: String): LocalDate?{
        var formatter = DateTimeFormatter.ofPattern("dd/M/yyyy")
        return LocalDate.parse(string,formatter)
    }

}