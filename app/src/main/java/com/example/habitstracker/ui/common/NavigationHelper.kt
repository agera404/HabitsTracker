package com.example.habitstracker.viewmodels


import androidx.lifecycle.MutableLiveData
import com.example.habitstracker.Event
import com.example.habitstracker.ui.common.interfaces.INavigation
import com.example.habitstracker.utils.NavDestinationID

class NavigationHelper : INavigation {
    private val _navigateEvent = MutableLiveData<Event<Any>>()
    override val navigateEvent = _navigateEvent

    override fun navToMainFragment() {
        navigateEvent.value = Event(NavDestinationID.MAIN_FRAGMENT)
    }

    override fun navToHabitFragment() {
        navigateEvent.value = Event(NavDestinationID.HABIT_FRAGMENT)
    }

    override fun navToCreateNewHabitDialog() {
        navigateEvent.value = Event(NavDestinationID.CREATE_NEW_HABIT_DIALOG_FRAGMENT)
    }

    override fun navToEditDatesDialog() {
        navigateEvent.value = Event(NavDestinationID.EDIT_DATES_DIALOG_FRAGMENT)
    }



    override fun navToTimePickerDialog() {
        navigateEvent.value = Event(NavDestinationID.TIME_PICKER_FRAGMENT)
    }

    override fun navToShowLocalCalendars() {
        navigateEvent.value = Event(NavDestinationID.SHOW_LOCAL_CALENDARS_DIALOG_FRAGMENT)
    }
}
