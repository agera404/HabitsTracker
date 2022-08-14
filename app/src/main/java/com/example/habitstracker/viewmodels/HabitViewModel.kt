package com.example.habitstracker.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.common.LocalCalendarUtility
import com.example.habitstracker.common.Notificator
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.EventEntity
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
import java.util.*

class HabitViewModel : ViewModel(), INavigationVM by NavigationVM() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate
    var idCalendar: Int?
        get() {
        return habitWDate.value?.calendarId
    }
        private set(value) {}


    fun writeToCalendar(context: Context){
        if (idCalendar != null){
            if (habitWDate.value!!.getDateEntityByDate(LocalDate.now()) != null){
                Log.d("Calendar_debug","Calendar ID is not null")
                val event = LocalCalendarUtility(context).createEvent(idCalendar!!, Calendar.getInstance(), habitWDate.value!!.habitName)
                val eventId = LocalCalendarUtility(context).addEventToCalendar(event)
                if (eventId != null){
                    viewModelScope.launch {
                        HabitsRepository.insertEvent(EventEntity(null, eventId, habitWDate.value!!.habitId!!, idCalendar!!))
                    }
                }
            }
        }else{
            Log.d("Calendar_debug","Calendar ID is null")
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