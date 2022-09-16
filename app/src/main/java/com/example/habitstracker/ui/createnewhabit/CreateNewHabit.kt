package com.example.habitstracker.ui.createnewhabit

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val insertHabit: (String) -> Unit = {
        habitDialogViewModel.insertNewHabit(it)
    }
    var isSaveButtonClicked by remember {
        mutableStateOf(false)
    }
    var isInsertError by remember {
        mutableStateOf(false)
    }
    val placeholderForName = stringResource(id = R.string.example_of_habit_name)
    val labelForName = stringResource(id = R.string.name)
    if (id.value != null && isSaveButtonClicked){
        isInsertError = false
        if (time.isNotBlank()){
            setNotify(time)
        }
        navController.popBackStack()
    }else if (isSaveButtonClicked && id.value == null){
        Toast.makeText(LocalContext.current, stringResource(id = R.string.error), Toast.LENGTH_SHORT)
        isInsertError = true
        isSaveButtonClicked = false
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
                        value = name,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { name = it },
                        label = { Text(labelForName) },
                        placeholder = {Text(text = placeholderForName)},
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            errorBorderColor = MaterialTheme.colors.error
                        ), isError = isInsertError
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
                            isSaveButtonClicked = true
                        }
                    }) {
                        Text(stringResource(id = R.string.save_button))
                    }
                }
            }
        }

    }
}