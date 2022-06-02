package com.example.habitstracker.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.common.Notificator
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel : ViewModel(), INavigationVM by NavigationVM() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate


    fun setHabitName(name: String) {
        val updatedHabit =  HabitEntity(habitWDate.value?.habitId!!,name)
        viewModelScope.launch {
            HabitsRepository.updateHabit(updatedHabit)
        }
    }

    fun navigateToEditDates() {
        navToEditDatesDialog()
    }

    fun setItem(id: Long) {
        val stateIn = HabitsRepository.getHabitWithDates(id).distinctUntilChanged().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000),null)
        viewModelScope.launch {
            stateIn.collect(){
                _habitWDate.setValue(it)
            }
        }
    }


}