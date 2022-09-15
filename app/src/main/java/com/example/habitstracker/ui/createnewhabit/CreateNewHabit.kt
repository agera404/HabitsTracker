package com.example.habitstracker.ui.createnewhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.habitstracker.R
import com.example.habitstracker.ui.notification.NotificationSettings
import com.example.habitstracker.ui.notification.NotificationSettingsViewModel

@Composable
fun CreateNewHabit(navController: NavController) {
    val habitDialogViewModel: CreateNewHabitDialogViewModel = viewModel()
    var name by remember { mutableStateOf("") }
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
                    // for name of habit
                    OutlinedTextField(
                        value = name, modifier = Modifier.fillMaxWidth(),
                        onValueChange = { name = it },
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
                    // notification field
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

                    //back button
                    OutlinedButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(id = R.string.back_button))
                    }
                    //save button
                    Button(onClick = {
                        if (name.isNotBlank()){
                            insertHabit(name)
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