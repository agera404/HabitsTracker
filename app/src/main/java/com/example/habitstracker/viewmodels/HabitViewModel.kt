package com.example.habitstracker.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.CalendarWriteAndRemove
import com.example.habitstracker.viewmodels.common.ICalendarWriteAndRemove
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel : ViewModel(), INavigationVM by NavigationVM(), ICalendarWriteAndRemove by CalendarWriteAndRemove() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate
    var idCalendar: Int?
        get() {
        return habitWDate.value?.calendarId
    }
        private set(value) {}


    fun writeToCalendar(context: Context){
        if (idCalendar != null && habitWDate.value!!.getDateEntityByDate(LocalDate.now()) != null) {
            viewModelScope.launch {
                //writeToCalendar(context, habitWDate.value!!.habitEntity)
            }
        }
    }

    fun setHabitName(name: String) {
        if (habitWDate.value?.habitId != null){
            val updatedHabit =  HabitEntity(habitWDate.value?.habitId!!,name, habitWDate.value?.habitEntity!!.calendar_id)
            viewModelScope.launch {
                HabitsRepository.updateHabit(updatedHabit)
            }
        }

    }



    fun navigateToEditDates() {
        navToEditDatesDialog()
    }
    fun navigateToShowLocalCalendars(){
        navToShowLocalCalendars()
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