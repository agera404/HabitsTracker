package com.example.habitstracker.ui.createnewhabit

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstracker.R
import com.example.habitstracker.ui.notification.NotificationSettings
import com.example.habitstracker.ui.notification.NotificationSettingsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@Composable
fun CreateNewHabit(navController: NavController) {
    val habitDialogViewModel: CreateNewHabitDialogViewModel = viewModel()
    var text by remember { mutableStateOf("") }
    var id = habitDialogViewModel.id
    val notificationViewModel: NotificationSettingsViewModel =  hiltViewModel()
    var time: String by remember {
        mutableStateOf("")
    }
    val setNotify: (String)-> Unit = { _time ->
        if (_time.isNotBlank() && id.value != null){
            notificationViewModel.setNotification(_time, id.value!!)
        }
    }
    val insertHabit: (String) -> MutableState<Long?> = {
        habitDialogViewModel.insertNewHabit(it)
        habitDialogViewModel.id
    }
    var isSaveButtonClicked by remember {
        mutableStateOf(false)
    }
    if (id.value != null && isSaveButtonClicked){
        setNotify(time)
    }
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Surface {
            Column(Modifier.padding(15.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = text, modifier = Modifier.fillMaxWidth(),
                        onValueChange = { text = it },
                        label = { Text(stringResource(id = R.string.name)) },
                        placeholder = {Text(text = stringResource(id = R.string.example_of_habit_name))}
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NotificationSettings(
                        selectedTime = null,
                        { _time: String ->
                            time = _time
                        }
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()) {

                    OutlinedButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(id = R.string.back_button))
                    }

                    Button(onClick = {
                        if (text.isNotBlank()){
                            insertHabit(text)
                            if (time.isNotBlank()){
                                isSaveButtonClicked = true
                            }
                            navController.popBackStack()
                        }
                    }) {
                        Text(stringResource(id = R.string.save_button))
                    }
                }
            }
        }

    }
}