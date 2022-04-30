package com.example.habitstracker.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    var _habitsList: MutableLiveData <List<HabitWDate>> = MutableLiveData <List<HabitWDate>>()
    val habitsList: LiveData <List<HabitWDate>>
        get() = _habitsList
    init {
        getHabitsList()
    }
    fun getHabitsList()
    {
        viewModelScope.launch{
            HabitsRepository.getAllHabits().collect(){ list ->
                _habitsList.value = list
            }
        }
    }
    fun checkDate(habit: Habit): Boolean{
        var dates: List<DateWhenCompleted> = listOf()
        for (item in habitsList.value!!){
            if (item.habit.id_habit == habit.id_habit){
                dates = item.dates
            }
        }
        for (date in dates){
            if (DateConverter.dateToString(date.date) == DateConverter.dateToString(Date()))
            {
                return true
            }
        }
        return false
    }
    fun insertTodayDate(habit: Habit){
        runBlocking {
            habit.id_habit?.let { HabitsRepository.insertDate(it,Date()) }
        }
    }
    fun deleteTodayDate(date: DateWhenCompleted){
        HabitsRepository.deleteDate(date)
    }
    fun deleteHabit(habit: Habit){
        HabitsRepository.deleteHabit(habit)
    }
}