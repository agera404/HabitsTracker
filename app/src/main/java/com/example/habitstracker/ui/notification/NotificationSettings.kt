package com.example.habitstracker.ui.notification

import android.app.TimePickerDialog
import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun NotificationSettings(
    selectedTime: LocalTime?,
    passTime: (String) -> Unit,
    viewModel: NotificationSettingsViewModel? = null,
    id: Long? = null
) {


    val selectedTimeValue = selectedTime?.format(
        DateTimeFormatter.ofLocalizedTime(
            FormatStyle.SHORT
        )
    )

    val calendar = Calendar.getInstance()
    var hour = calendar[Calendar.HOUR_OF_DAY]
    var minute = calendar[Calendar.MINUTE]

    if (selectedTimeValue != null) {
        hour = selectedTime.hour
        minute = selectedTime.minute
    }

    var time by remember {
        mutableStateOf("")
    }
    if (viewModel != null && id != null && time.isBlank()) {
        val notifyInfo = viewModel.getNotificationInfo(id)
        if (notifyInfo != null) {
            time = LocalTime.ofSecondOfDay(notifyInfo.time)
                .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        }
    }

    val picker = TimePickerDialog(
        LocalContext.current,
        TimePickerDialog.OnTimeSetListener { _, _hour: Int, _minute: Int ->
            val _time = LocalTime.of(_hour, _minute)
            time = _time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        },
        hour, minute, true
    )

    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            var text by remember {
                mutableStateOf("")
            }
            if (time.isBlank()) {
                text = "Off"
            } else {
                text = time
            }

            picker.setOnDismissListener {
                if (time.isNotBlank()) {
                    passTime(time)
                } else {
                    if (viewModel != null && id != null) {
                        viewModel.removeNotificationInfo(id)
                    }
                }
            }
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = { Text("Reminder") },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            picker.show()
                        }, trailingIcon = {
                        if (text != "Off"){
                            IconButton(onClick = {
                                if (viewModel != null && id != null){
                                    viewModel.removeNotificationInfo(id)
                                    text = "Off"
                                    time = ""
                                }
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete reminder")
                            }
                        }
                    }
                )
            }

    }
}


