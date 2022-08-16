package com.example.habitstracker.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateEntity
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
import java.util.*

class EditDatesViewModel : ViewModel(),
    INavigationVM by NavigationVM(),
    ICalendarWriteAndRemove by CalendarWriteAndRemove() {
    var idHabit: Long = 0
    lateinit var context: Context
    val dataSet: List<LocalDate>?
    get() {
        return habitWDate.value?.listOfDates
    }
    fun init(idHabit: Long, context: Context){
        this.idHabit = idHabit
        this.context = context
        setItem(idHabit)
    }

    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate

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
            if (isDateExist(date, idHabit)){
                removeDate(idHabit, date, context)
            }else{
                insertDate(idHabit, date, context)
                setItem(idHabit)
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