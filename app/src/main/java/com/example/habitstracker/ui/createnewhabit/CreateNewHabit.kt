package com.example.habitstracker.ui.createnewhabit

import android.util.Log
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
import com.example.habitstracker.ui.theme.AppTheme

@Composable
fun CreateNewHabit(navController: NavController) {
    val viewModel: CreateNewHabitDialogViewModel = viewModel()
    var text by remember { mutableStateOf("") }
    var id: Long? by remember {
        mutableStateOf(null)
    }
    val insertHabit: (String) -> Long? = {
        Log.d("dLog","insertHabit($it)")
        viewModel.insertNewHabit(it)
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
                    val viewModel: NotificationSettingsViewModel =  hiltViewModel()
                    NotificationSettings(
                        selectedTime = null,
                        { time: String ->
                            if (id == null && text.isNotBlank()){
                                id = insertHabit(text)
                            }
                            if (id!=null){
                                Log.d("dLog","if (id!=null)")
                                viewModel.setNotification(time, id!!)
                            }
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
                            if (id == null){
                                id = viewModel.insertNewHabit(name = text)
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