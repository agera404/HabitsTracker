package com.example.habitstracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.common.LocalCalendarUtility
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.LocalCalendar
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.launch

class ShowLocalCalendarViewModel : ViewModel() {
    private var selectedCalendarId: Int? = null
    var habit_id: Long? = null
    fun getLocalCalendars(context: Context): List<LocalCalendar?> {
        val array = LocalCalendarUtility(context).getAllAvailableCalendars()
        return array.toList()
    }
    private fun updateHabit(){
        val habit = habit_id?.let { HabitsRepository.getHabitById(it) }
        if (habit != null){
            val updatedHabit = HabitEntity(habit.id_habit, habit.name, selectedCalendarId)
            viewModelScope.launch {
                HabitsRepository.updateHabit(updatedHabit)
            }
        }
    }
    fun setCalendarId(id: Int?){
        selectedCalendarId = id
        updateHabit()
    }

}