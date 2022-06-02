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
        return dates.firstOrNull{DateConverter.toString(date) == date_str}
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