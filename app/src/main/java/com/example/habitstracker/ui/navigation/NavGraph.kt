package com.example.habitstracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habitstracker.ui.createnewhabit.CreateNewHabit
import com.example.habitstracker.ui.editdates.EditHistoryMapScreen
import com.example.habitstracker.ui.habit.Habit
import com.example.habitstracker.ui.main.Main
import com.example.habitstracker.ui.navigation.Screens
import com.example.habitstracker.ui.showlocalcalendars.ShowLocalCalendars

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route)
    {
        composable(route = Screens.CreateNewHabit.route){ backStackArgument ->
            CreateNewHabit(navController)}
        composable(route = Screens.Main.route){
            Main(navController)
        }
        composable(route = Screens.Habit.route+"/{habitId}"){ backStackArgument ->
            Habit(navController, backStackArgument.arguments?.getString("habitId"))
        }
        composable(Screens.EditHistoryMap.route+"/{habitId}"){ backStackArgument ->
            EditHistoryMapScreen(navController = navController, id = backStackArgument.arguments?.getString("habitId"))
        }
        composable(Screens.ShowLocalCalendars.route+"/{habitId}"){backStackArgument ->
            ShowLocalCalendars(navController,id=backStackArgument.arguments?.getString("habitId"))
        }
    }
}