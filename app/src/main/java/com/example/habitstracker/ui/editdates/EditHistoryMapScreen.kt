package com.example.habitstracker.ui.editdates

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitstracker.R
import com.example.habitstracker.ui.habit.ShowCanvas
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditHistoryMapScreen(navController: NavController, id: String?) {
    var flag =  remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = {
        if (!flag.value) {
            navController.navigateUp()
            flag.value = true
        }
    }) {
        Surface(
            Modifier
                .fillMaxWidth()
        ) {
            val _id = id?.toLong()

            if (_id != null) {
                val viewModel: EditDatesViewModel = hiltViewModel()
                viewModel.init(_id)

                var _list = viewModel.listOfDates.value

                var date by remember {
                    mutableStateOf(LocalDate.now())
                }
                Column(
                    Modifier
                        .fillMaxWidth()

                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(310.dp), verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(10.dp)) {
                            ShowCanvas(
                                list = _list,
                                { date: LocalDate ->
                                    Log.d("dLog", "changeDataStatus("+date.toString()+")")
                                    viewModel.changeDataStatus(date)
                                }, isLarge = true, date
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.height(20.dp)) {
                                IconButton(onClick = { date = date.withDayOfMonth(1).minusDays(1) }) {
                                    Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Arrow Left")
                                }
                            }
                            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.height(20.dp)) {
                                Text(text = date.month.name +" "+date.year)
                            }

                            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.height(20.dp)) {
                                IconButton(enabled = date.month.value != LocalDate.now().monthValue,
                                onClick = {
                                    if (date.month.value != LocalDate.now().monthValue) {
                                        var temp_date =
                                            date.plusMonths(2).withDayOfMonth(1).minusDays(1)
                                        if (temp_date.month == LocalDate.now().month) {
                                            date = LocalDate.now()
                                            return@IconButton
                                        }
                                        date = temp_date
                                    }
                                }) {
                                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Arrow Right")
                                }
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 10.dp)) {
                        Button(onClick = {navController.navigateUp()}, modifier = Modifier.fillMaxWidth()) {
                            Text(text = stringResource(id = R.string.back_button))
                        }
                    }
                }
            }
        }
    }
}