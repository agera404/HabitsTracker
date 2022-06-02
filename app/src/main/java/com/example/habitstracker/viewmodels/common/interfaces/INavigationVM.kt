package com.example.habitstracker.viewmodels.common.interfaces

import androidx.lifecycle.LiveData
import com.example.habitstracker.Event

interface INavigationVM{
    val navigateEvent: LiveData<Event<Any>>

    fun navToMainFragment()
    fun navToHabitFragment()
    fun navToCreateNewHabitDialog()
    fun navToEditDatesDialog()
    fun navToDatePicker()
    fun navToTimePickerDialog()
}