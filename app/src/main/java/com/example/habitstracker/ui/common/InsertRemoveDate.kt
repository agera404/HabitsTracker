package com.example.habitstracker.ui.common

import androidx.lifecycle.viewModelScope
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class InsertRemoveDate @Inject constructor(private val calendarHelper: CalendarWriteAndRemove) {
    fun isDateExist(idHabit: Long, date: LocalDate): Boolean {
        return HabitsRepository.isDateExist(date, idHabit)
    }
    suspend fun insertDate(idHabit: Long, date: LocalDate) {
        HabitsRepository.insertDate(idHabit, date)
        val habitEntity = HabitsRepository.getHabitById(idHabit)
        if (habitEntity != null) {
            calendarHelper.writeToCalendar(habitEntity, date)
        }
    }
    fun removeDate(idHabit: Long, date: LocalDate) {
        val id = HabitsRepository.findDate(date, idHabit)
        if (id != null) {
            HabitsRepository.removeDate(DateEntity(id, date, idHabit))
        }
        val habitEntity = HabitsRepository.getHabitById(idHabit)
        val eventEntity = HabitsRepository.getEventEntityByParameters(
            habitEntity?.calendar_id,
            idHabit,
            DateConverter.toString(date)!!
        )
        eventEntity?.id_event?.let { calendarHelper.removeFromCalendar(it) }
    }
}