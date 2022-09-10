package com.example.habitstracker.ui.navigation

sealed class Screens(val route: String) {
    object CreateNewHabit: Screens("create_new_habit")
    object Main: Screens ("main")
    object Habit: Screens ("habit")
    object EditHistoryMap: Screens ("edit_history_map")
    object ShowLocalCalendars: Screens("show_local_calendars")

    fun withArg(vararg args: String): String{
        return  buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}