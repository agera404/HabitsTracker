package com.example.habitstracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.habitstracker.data.database.AppDatabase
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.ui.common.interfaces.INavigation
import com.example.habitstracker.viewmodels.NavigationHelper
import java.time.LocalDate
import java.time.LocalTime


class ActivityViewModel(application: Application) : AndroidViewModel(application),
    INavigation by NavigationHelper() {

    private val context = application.applicationContext
    init {
        HabitsRepository.db = AppDatabase.getInstance(context)
    }

    private var _dates: MutableLiveData<List<LocalDate>> = MutableLiveData()
    val dates: LiveData<List<LocalDate>> = _dates

    fun setDates(dates: List<LocalDate>){
        _dates.value = dates
    }
    fun getDates(): List<LocalDate>? {
        return _dates.value
    }


    private var _selectedTime: MutableLiveData<LocalTime> = MutableLiveData<LocalTime>()
    val selectedTime: LiveData<LocalTime> = _selectedTime

    fun getSelectedTime(): LocalTime?{
        return  _selectedTime.value
    }
    fun setSelectedTime(time: LocalTime){
        _selectedTime.value = time
    }

    private var _selectedDate: MutableLiveData<LocalDate> = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun getSelectedDate(): LocalDate?{
        return  _selectedDate.value
    }
    fun setSelectedDate(date: LocalDate){
        _selectedDate.value = date
    }

    var _itemId: MutableLiveData<Long> = MutableLiveData<Long>()
    val itemId: LiveData<Long> = _itemId

    fun getItemId(): Long? {
        return _itemId.getValue()
    }

    fun setItemId(id: Long) {
        this._itemId.setValue(id)
    }

    fun navigateToCreateNewHabit(){
       this.navToCreateNewHabitDialog()
    }

}