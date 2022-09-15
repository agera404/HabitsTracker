package com.example.habitstracker.ui.habit

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitstracker.ui.notification.NotificationSettings
import com.example.habitstracker.R
import com.example.habitstracker.ui.HistoryMap
import com.example.habitstracker.ui.navigation.Screens
import com.example.habitstracker.ui.notification.NotificationSettingsViewModel
import java.time.LocalDate

@Composable
fun Habit(navController: NavController, idStr: String?) {
    val id = idStr?.toLong()
    if (id != null) {
        val viewModel: HabitViewModel = hiltViewModel()
        viewModel.setItem(id)
        Scaffold(topBar = {
            HabitToolBar(
                viewModel.habitName.value,
                navController = navController
            )
        },
            content = { it ->
                Column(Modifier.padding(it)) {
                    HabitContent(viewModel, navController)
                }
            })
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HabitContent(viewModel: HabitViewModel, navController: NavController) {
    val context = LocalContext.current
    var requirePermissionString = stringResource(id = R.string.require_permission)
    Surface(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Row(Modifier.fillMaxWidth()) {
                //name of habit
                var name by remember {
                    mutableStateOf("")
                }
                if (name.isBlank()) {
                    name = viewModel.habitName.value
                }
                val destination by remember {
                    mutableStateOf(navController.currentDestination)
                }
                if (destination != navController.currentDestination) {
                    viewModel.setHabitName(name)
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    Modifier
                        .fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.name)) },
                    placeholder = { Text(stringResource(id = R.string.example_of_habit_name)) })
            }
            //reminder
            Row(Modifier.fillMaxWidth()) {
                if (viewModel.idHabit != null) {
                    val notifyViewModel: NotificationSettingsViewModel = hiltViewModel()
                    NotificationSettings(notifyViewModel.getSelectedTime(viewModel.idHabit!!),
                        { time: String ->
                            notifyViewModel.setNotification(time, viewModel.idHabit!!)
                        }, notifyViewModel, viewModel.idHabit)
                }
            }
            //calendar button
            var onSelectCalendarButtonClick = {}
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    onSelectCalendarButtonClick()
                } else {
                    Toast.makeText(context, requirePermissionString, Toast.LENGTH_LONG)
                }
            }
            onSelectCalendarButtonClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED){
                    navController.navigate(
                        Screens.ShowLocalCalendars.withArg(
                            viewModel.idHabit.toString()
                        )
                    )
                }else if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED){
                    launcher.launch(Manifest.permission.READ_CALENDAR)
                }else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED){
                    launcher.launch(Manifest.permission.WRITE_CALENDAR)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = {
                    onSelectCalendarButtonClick()
                }, Modifier.fillMaxWidth()) {
                    Image(
                        painterResource(
                            id = R.drawable.ic_baseline_calendar_month_24
                        ),
                        contentDescription = "calendar icon"
                    )
                    val calendarName: String = viewModel.calendarName ?: stringResource(id = R.string.dont_add_to_calendar_button)
                    Text(text = calendarName)
                }
            }
            //history map title
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.history_map), fontSize = 22.sp)
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(onClick = {
                    navController.navigate(
                        Screens.EditHistoryMap.withArg(
                            viewModel.idHabit!!.toString()
                        )
                    )
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                        contentDescription = "edit button"
                    )
                }
            }
            //history map
            Row(Modifier.fillMaxWidth()) {
                //canvas
                ShowCanvas(
                    list = viewModel.listOfDates ?: listOf(),
                    { date: LocalDate ->
                        viewModel.changeDataStatus(date)
                    },
                    false
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun ShowCanvas(
    list: List<LocalDate>,
    passDate: (date: LocalDate) -> Unit,
    isLarge: Boolean,
    date: LocalDate = LocalDate.now()
) {
    var action by remember {
        mutableStateOf(false)
    }
    var pairXY = (Pair<Float, Float>(0f, 0f))

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .pointerInteropFilter {
            if (isLarge) {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    val x = it.x
                    val y = it.y
                    pairXY = Pair(x, y)
                    action = true
                }
            }
            true
        }) {
        val historyMap = HistoryMap(isLarge, list, size.width, date = date)
        val items = historyMap.arrayOfSad

        if (action) {
            for (item in items) {
                if (item.rectF.right < pairXY.first &&
                    pairXY.first < item.rectF.left &&
                    item.rectF.top < pairXY.second &&
                    pairXY.second < item.rectF.bottom
                ) {
                    if (item.date.isBefore(LocalDate.now()) || item.date.isEqual(LocalDate.now())) {
                        passDate(item.date)
                    }
                    action = false
                }
            }


        }

        if (isLarge) {
            val dayOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
            for (i in 1..7) {
                drawContext.canvas.nativeCanvas.drawText(
                    dayOfWeek[i - 1],
                    historyMap.START_X_CANVAS + 15f,
                    (historyMap.START_Y_CANVAS + ((historyMap.rec_size + historyMap.SPACE_BETWEEN_Y) * i) - 40),
                    items[0].textPaint
                )
            }
        }


        for (sad in items) {
            drawContext.canvas.nativeCanvas.drawRoundRect(
                sad.rectF,
                historyMap.ROUND_REC_RX_RY,
                historyMap.ROUND_REC_RX_RY,
                sad.paint
            )
            sad.drawText(drawContext.canvas.nativeCanvas)
            if (!isLarge) {
                if (sad.date.dayOfMonth < 8) {
                    if (sad.date.dayOfWeek.value == 1) {
                        drawContext.canvas.nativeCanvas.drawText(
                            sad.date.month.toString(),
                            sad.rectF.left + (historyMap.SPACE_BETWEEN_Y * 2),
                            historyMap.START_Y_CANVAS - 15,
                            sad.textPaint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HabitToolBar(title: String, navController: NavController) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}
