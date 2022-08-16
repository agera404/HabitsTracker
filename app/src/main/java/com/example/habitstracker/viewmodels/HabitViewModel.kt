package com.example.habitstracker.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateEntity
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
    val idHabit: Long?
        get() {
            return habitWDate.value?.habitId
        }
    lateinit var context: Context


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
    fun changeDataStatus(date: LocalDate){
        if (idHabit!=null){
            if (isDateExist(date, idHabit!!)){
                removeDate(idHabit!!, date, context)
            }else{
                insertDate(idHabit!!, date, context)
            }
        }

    }

    fun isDateExist(date: LocalDate, idHabit: Long):Boolean{
        return HabitsRepository.isDateExist(date, idHabit)
    }
    fun insertDate(idHabit: Long, date: LocalDate, context: Context){
        viewModelScope.launch {
            HabitsRepository.insertDate(idHabit, date)
            val habitEntity = HabitsRepository.getHabitById(idHabit)
            if (habitEntity != null) {
                writeToCalendar(context, habitEntity, date)
            }
        }
    }
    fun removeDate(idHabit: Long, date: LocalDate, context: Context){
        var id = HabitsRepository.findDate(date,idHabit)
        if (id != null){
            HabitsRepository.removeDate(DateEntity(id,date,idHabit))
        }
        val habitEntity = HabitsRepository.getHabitById(idHabit)
        val eventEntity = HabitsRepository.getEventEntityByParameters(
            habitEntity?.calendar_id,
            idHabit,
            DateConverter.toString(date)!!
        )
        eventEntity?.id_event?.let { removeFromCalendar(context, it) }
    }

}