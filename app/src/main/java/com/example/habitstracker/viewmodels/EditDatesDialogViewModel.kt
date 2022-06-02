package com.example.habitstracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class EditDatesDialogViewModel : ViewModel(), INavigationVM by NavigationVM() {

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    var idHabit: Long = 0

    init {
        _selectedDate.value = LocalDate.now()
    }
    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date
    }

    fun navigateToDatePicker(){
       navToDatePicker()
    }
    fun isDateExist(date: LocalDate, idHabit: Long):Boolean{
        return HabitsRepository.isDateExist(date, idHabit)
    }
    fun insertDate(idHabit: Long, date: LocalDate){
        viewModelScope.launch {
            HabitsRepository.insertDate(idHabit, date)
        }
    }
    fun removeDate(idHabit: Long, date: LocalDate){
        var id = HabitsRepository.findDate(date,idHabit)
        if (id != null){
            HabitsRepository.removeDate(DateEntity(id,date,idHabit))
        }

    }
}