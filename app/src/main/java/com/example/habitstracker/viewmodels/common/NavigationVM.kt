package com.example.habitstracker.viewmodels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.habitstracker.Event
import com.example.habitstracker.R
import com.example.habitstracker.common.NavDestinationID
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM

class NavigationVM : INavigationVM {
    private val _navigateEvent = MutableLiveData<Event<Any>>()
    override val navigateEvent = _navigateEvent

    override fun navToMainFragment() {
        navigateEvent.value = Event(NavDestinationID.MAIN_FRAGMENT.destinationID)
    }

    override fun navToHabitFragment() {
        navigateEvent.value = Event(NavDestinationID.HABIT_FRAGMENT.destinationID)
    }

    override fun navToCreateNewHabitDialog() {
        navigateEvent.value = Event(NavDestinationID.CREATE_NEW_HABIT_DIALOG_FRAGMENT.destinationID)
    }

    override fun navToEditDatesDialog() {
        navigateEvent.value = Event(NavDestinationID.EDIT_DATES_DIALOG_FRAGMENT.destinationID)
    }

    override fun navToDatePicker() {
        navigateEvent.value = Event(NavDestinationID.DATE_PICKER_FRAGMENT.destinationID)
    }

    override fun navToTimePickerDialog() {
        navigateEvent.value = Event(NavDestinationID.TIME_PICKER_FRAGMENT.destinationID)
    }
}
