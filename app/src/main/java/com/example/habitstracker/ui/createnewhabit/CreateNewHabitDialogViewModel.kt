package com.example.habitstracker.ui.createnewhabit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.data.repositories.HabitsRepository
import kotlinx.coroutines.launch

class CreateNewHabitDialogViewModel : ViewModel() {

    fun insertNewHabit(name: String): Long? {
        val result = MutableLiveData<Long>()
        viewModelScope.launch {
            val habitEntity: HabitEntity = HabitEntity(null,name)
           result.value = HabitsRepository.insertHabit(habitEntity)
        }
        return result.value
    }
}