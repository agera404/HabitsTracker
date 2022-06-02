package com.example.habitstracker.common
import com.example.habitstracker.R

enum class NavDestinationID(val destinationID: Int) {
    MAIN_FRAGMENT(R.id.mainFragment),
    HABIT_FRAGMENT(R.id.habitFragment),
    CREATE_NEW_HABIT_DIALOG_FRAGMENT(R.id.createNewHabitDialogFragment),
    EDIT_DATES_DIALOG_FRAGMENT(R.id.editDatesDialogFragment),
    DATE_PICKER_FRAGMENT(R.id.datePickerFragment),
    TIME_PICKER_FRAGMENT(R.id.timePickerFragment)
}