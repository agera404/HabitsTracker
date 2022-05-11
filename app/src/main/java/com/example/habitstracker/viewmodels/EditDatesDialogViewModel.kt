package com.example.habitstracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.launch
import java.util.*

class EditDatesDialogViewModel : ViewModel() {
    private val _navigateEvent = MutableLiveData<Event<Any>>()
    val navigateEvent: LiveData<Event<Any>> = _navigateEvent

    private val _selectedDate: MutableLiveData<Date> = MutableLiveData()
    val selectedDate: LiveData<Date> = _selectedDate

    var idHabit: Long = 0

    init {
        _selectedDate.value = Date()
    }
    fun setSelectedDate(date: Date){
        _selectedDate.value = date
    }

    fun navigateToDatePicker(){
        _navigateEvent.value = Event(R.id.datePickerFragment)
    }
    fun isDateExist(date: Date, idHabit: Long):Boolean{
        return HabitsRepository.isDateExist(date, idHabit)
    }
    fun insertDate(idHabit: Long, date: Date){
        viewModelScope.launch {
            HabitsRepository.insertDate(idHabit, date)
        }
    }
    fun removeDate(idHabit: Long, date: Date){
        var id = HabitsRepository.findDate(date,idHabit)
        if (id != null){
            HabitsRepository.removeDate(DateWhenCompleted(id,date,idHabit))
        }

    }
}