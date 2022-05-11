package com.example.habitstracker.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class HabitViewModel : ViewModel() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate

    private val _navigateEvent = MutableLiveData<Event<Any>>()
    val navigateEvent: LiveData<Event<Any>> = _navigateEvent

    fun subscribeHabit(){
        viewModelScope.launch {
            val id = habitWDate.value?.habit?.id_habit!!
            HabitsRepository.getHabitWithDates(id).collect(){
                _habitWDate.value = it
            }
        }
    }
    fun navigateToEditDates(){
        _navigateEvent.value = Event(R.id.editDatesDialogFragment)
    }

    fun setItem(habitWDate: HabitWDate){
        _habitWDate.value = habitWDate
    }
    fun getMinDate():Date{
        var listOfDates = habitWDate.value?.dates?.map { it.date }
        var minDate: Date = Date()
        if (listOfDates != null) {
            for (date in listOfDates){
                if (minDate.after(date)){
                    minDate = date
                }
            }
        }
        return minDate
    }
    fun getDates(): List<Date> {
        var listOfDates = habitWDate.value?.dates?.map { it.date }
        if (listOfDates != null) return listOfDates
        return listOf()
    }
}