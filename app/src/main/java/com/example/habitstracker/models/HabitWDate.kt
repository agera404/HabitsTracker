package com.example.habitstracker.models

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import java.time.LocalDate


data class HabitWDate(
    @Embedded var habitEntity: HabitEntity,
    @Relation(
        entity = DateEntity::class,
        parentColumn = "id_habit",
        entityColumn = "id_habit"
    )
    private var dates: List<DateEntity>) {
    val habitId: Long?
    get() {
        return habitEntity.id_habit
    }
    val habitName: String
    get() {
        return habitEntity.name
    }
    val calendarId: Int?
    get() {
        return habitEntity.calendar_id
    }

    val datesEntities: List<DateEntity>
    get() {
        return dates
    }
    val listOfDates: List<LocalDate>
    get() {
        return dates.map { it.date }
    }
    fun getDateEntityByDate(date: LocalDate): DateEntity? {
        val date_str = DateConverter.toString(date)
        for (_date in dates){
            if (date_str == DateConverter.toString(_date.date)){
                return _date
            }
        }
        return null
    }
    fun getMinDate(): LocalDate{
        var minDate: LocalDate = LocalDate.now()
        for (date in listOfDates) {
            if (minDate.isAfter(date)) {
                minDate = date
            }
        }
        return minDate
    }

}