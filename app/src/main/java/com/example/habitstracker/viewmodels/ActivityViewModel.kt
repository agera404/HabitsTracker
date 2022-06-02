package com.example.habitstracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import java.time.LocalDate
import java.time.LocalTime


class ActivityViewModel(application: Application) : AndroidViewModel(application), INavigationVM by NavigationVM() {

    private val context = application.applicationContext
    init {
        HabitsRepository.db = AppDatabase.getInstance(context)
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