package com.example.habitstracker.ui.common.interfaces

import androidx.lifecycle.LiveData
import com.example.habitstracker.Event

interface INavigation {
    val navigateEvent: LiveData<Event<Any>>
    fun navToMainFragment()
    fun navToHabitFragment()
    fun navToCreateNewHabitDialog()
    fun navToEditDatesDialog()
    fun navToTimePickerDialog()
    fun navToShowLocalCalendars()
}