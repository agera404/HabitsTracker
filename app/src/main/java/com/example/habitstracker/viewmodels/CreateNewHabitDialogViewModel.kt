package com.example.habitstracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.launch

class CreateNewHabitDialogViewModel : ViewModel() {
    init {

    }
    fun insertNewHabit(name: String): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch {
            val habitEntity: HabitEntity = HabitEntity(null,name)
           result.value = HabitsRepository.insertHabit(habitEntity)
        }
        return result
    }
}