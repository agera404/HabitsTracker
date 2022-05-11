package com.example.habitstracker.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class MainViewModel : ViewModel() {

    private var _habitsList: MutableLiveData<List<HabitWDate>> = MutableLiveData<List<HabitWDate>>()
    val habitsList: LiveData<List<HabitWDate>> = _habitsList

    private val _navigateEvent = MutableLiveData<Event<Any>>()
    val navigateEvent: LiveData<Event<Any>> = _navigateEvent

    private var _selectedItem: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var selectedItem: LiveData<HabitWDate> = _selectedItem

    init {
        getHabitsList()
    }

    private fun getHabitsList() {
        viewModelScope.launch {
            HabitsRepository.getAllHabits().collect() { list ->
                _habitsList.value = list
            }
        }
    }

    fun isDateExist(habit: Habit): Boolean {

        val datesWhenCompleted =
            habitsList.value!!.stream().filter { it.habit.id_habit == habit.id_habit }
                .map { it.dates }
        val dates = datesWhenCompleted.flatMap { it.stream().map { it.date } }

        return dates.anyMatch { DateConverter.dateToString(it) == DateConverter.dateToString(Date()) }
    }

    fun insertTodayDate(habit: Habit) {
        runBlocking {
            habit.id_habit?.let { HabitsRepository.insertDate(it, Date()) }
        }
    }

    fun removeTodayDate(date: DateWhenCompleted) {
        HabitsRepository.removeDate(date)
    }

    fun deleteHabit(habit: Habit) {
        HabitsRepository.deleteHabit(habit)
    }

    fun navigateToHabitFragment(habitWDate: HabitWDate) {
        _navigateEvent.value = Event(R.id.action_mainFragment_to_habitFragment)
        _selectedItem.value = habitWDate
    }
}