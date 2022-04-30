package com.example.habitstracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.models.Habit
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.launch

class CreateNewHabitDialogViewModel : ViewModel() {
    init {

    }
    fun insertNewHabit(name: String){
        viewModelScope.launch {
            val habit: Habit = Habit(null,name)
            HabitsRepository.insertHabit(habit)
        }
    }
}