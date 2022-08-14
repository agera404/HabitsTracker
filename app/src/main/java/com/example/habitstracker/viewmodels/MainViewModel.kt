package com.example.habitstracker.viewmodels


import android.content.Context
import androidx.lifecycle.*
import com.example.habitstracker.models.*
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.CalendarWriteAndRemove
import com.example.habitstracker.viewmodels.common.ICalendarWriteAndRemove
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate


class MainViewModel : ViewModel(), INavigationVM by NavigationVM(), ICalendarWriteAndRemove by CalendarWriteAndRemove() {

    private var _habitsList: MutableLiveData<List<HabitWDate>> = MutableLiveData<List<HabitWDate>>()
    val habitsList: LiveData<List<HabitWDate>> = _habitsList

    private var _selectedItem: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var selectedItem: LiveData<HabitWDate> = _selectedItem

    init {
        getHabitsList()
    }

    private fun getHabitsList() {
        val stateIn = HabitsRepository.getAllHabits().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000),null)
        viewModelScope.launch{
            _habitsList = stateIn.collect {
                _habitsList.postValue(it)
            }
        }
    }

    fun isDateExist(idhabit: Long, date: LocalDate): Boolean {
        val habitWDate = habitsList.value!!.first{it.habitId == idhabit}
        if (habitWDate.getDateEntityByDate(date) == null){
            return false
        }
        return true
    }
    var context :Context? = null
    fun insertTodayDate(habitEntity: HabitEntity) {
        runBlocking {
            habitEntity.id_habit?.let { HabitsRepository.insertDate(it, LocalDate.now()) }
            writeToCalendar(context!!, habitEntity, LocalDate.now())
        }
    }

    fun removeTodayDate(date: DateEntity) {
        HabitsRepository.removeDate(date)
        val habit = HabitsRepository.getHabitById(date.id_habit)
        if(habit?.calendar_id != null){
            val eventEntity = HabitsRepository.getEventEntityByParameters(habit.calendar_id,
                habit.id_habit!!, DateConverter.toString(date.date)!!
            )
            if (eventEntity != null){
                removeFromCalendar(context!!, eventEntity.id_event!!)
            }
        }

    }

    fun deleteHabit(habitEntity: HabitEntity) {
        HabitsRepository.deleteHabit(habitEntity)
    }

    fun navigateToHabitFragment(habitWDate: HabitWDate) {
        navToHabitFragment()
        _selectedItem.value = habitWDate
    }
}