package com.example.habitstracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.models.HabitWDate
import java.util.*


class ActivityViewModel: ViewModel() {
    private val _editDateState: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>(false)
    val editDateState: LiveData<Boolean?> = _editDateState

    fun getEditDateState(): Boolean? {
        return _editDateState.value
    }
    fun setEditDateState(boolean: Boolean?){
        _editDateState.value = boolean
    }

    private var _selectedDate: MutableLiveData<Date> = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    fun getSelectedDate(): Date?{
        return  _selectedDate.value
    }
    fun setSelectedDate(date: Date){
        _selectedDate.value = date
    }

    var _item: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    val item: LiveData<HabitWDate> = _item

    fun getItem(): HabitWDate? {
        return _item.getValue()
    }

    fun setItem(item: HabitWDate) {
        this._item.setValue(item)
    }

    private val _navigateEvent = MutableLiveData<Event<Any>>()
    val navigateEvent: LiveData<Event<Any>> = _navigateEvent

    fun navigateToCreateNewHabit(){
        _navigateEvent.value = Event(R.id.createNewHabitDialogFragment)
    }

}