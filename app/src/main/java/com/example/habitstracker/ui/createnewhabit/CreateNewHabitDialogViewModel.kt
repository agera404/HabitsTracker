package com.example.habitstracker.ui.createnewhabit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.data.repositories.HabitsRepository
import kotlinx.coroutines.launch

class CreateNewHabitDialogViewModel : ViewModel() {

    private val _id: MutableState<Long?> = mutableStateOf(null)
    val id: MutableState<Long?>
        get() = _id


    fun insertNewHabit(name: String) {
        val id: MutableState<Long?> = mutableStateOf(null)
        viewModelScope.launch {
            val habitEntity: HabitEntity = HabitEntity(null,name)
           _id.value = HabitsRepository.insertHabit(habitEntity)
        }
    }
}