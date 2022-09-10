package com.example.habitstracker.ui.showlocalcalendars

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstracker.R
import com.example.habitstracker.models.LocalCalendar
import com.example.habitstracker.ui.habit.HabitViewModel
import com.example.habitstracker.ui.navigation.Screens

@Composable
fun ShowLocalCalendars(navController: NavController, id: String?) {

    if (id != null) {
        val viewModel: ShowLocalCalendarViewModel = viewModel()
        val calendars = viewModel.getLocalCalendars(LocalContext.current)
        val _id = id.toLong()
        viewModel.idHabit = _id
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            , verticalAlignment = Alignment.Top) {
                        LazyColumn() {
                            items(calendars) { item ->
                                if (item != null) {
                                    Row(
                                        Modifier
                                            .clickable {
                                                viewModel.setCalendarId(item.id)
                                                navController.navigateUp()
                                            }
                                            .padding(10.dp)
                                            .fillMaxWidth()) {
                                        Text(text = item.name, fontSize = 20.sp)
                                    }
                                    Divider(color = Color.DarkGray)
                                }
                            }
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp), verticalAlignment = Alignment.Bottom) {
                        Button(onClick = {
                            navController.navigateUp()
                                         }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = stringResource(id = R.string.back_button))
                        }
                }
                }
            }
        }
    }
}