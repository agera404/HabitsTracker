package com.example.habitstracker.models

import androidx.room.Embedded
import androidx.room.Relation


data class HabitWDate(
    @Embedded var habit: Habit,
    @Relation(
        entity = DateWhenCompleted::class,
        parentColumn = "id_habit",
        entityColumn = "id_date"
    )
    var dates: List<DateWhenCompleted>) {
    }