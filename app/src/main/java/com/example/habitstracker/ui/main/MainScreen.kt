package com.example.habitstracker.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitstracker.R
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.ui.navigation.Screens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Main(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val habits = viewModel.habitsListState

    // Action on swipe card habit
    val dismissAction: (HabitWDate) -> Unit = {
        viewModel.deleteHabit(it.habitEntity)
        habits.remove(it)
    }

    // Action on click card habit
    val clickCardAction: (HabitWDate) -> Unit = {
        navController.navigate(Screens.Habit.withArg(it.habitId.toString()))
    }

    // Action on click past days on habit
    val clickDayAction: (HabitWDate) -> Unit = {
        val habitId = it.habitId
        if (habitId != null) {
            val result = viewModel.isDateExist(habitId, LocalDate.now())
            if (result != null)
                if (result) {
                    viewModel.removeTodayDate(habitId)
                } else {
                    viewModel.insertTodayDate(it.habitEntity)
                }
        }
    }

    // Get drawable resource on status habit
    val getResourceID: (HabitWDate, LocalDate) -> Int? = { hw, date ->
        val isDone = viewModel.isDateExist(hw.habitId!!, date)
        if (isDone != null)
            if (isDone) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        else null
    }

    // Main block view
    Scaffold(topBar = { MainToolBar(navController = navController) }, content = {
        Surface(
            modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
        ) {
            LazyColumn() {
                // Create view on all habits
                items(habits) { item ->
                    // Fixed bug in swipe
                    key(item) {
                        // Create view Swipe
                        SwipeCardView(item, dismissAction) {
                            // Create view card on habit
                            CardView(item,
                                     clickCardAction,
                                     clickDayAction,
                                     getResourceID,
                                     viewModel.getPastThreeDays())
                        }
                    }
                }
            }
        }
    })
}

// Template view on habit
@Composable
fun CardView(item: HabitWDate,
             clickCard: (HabitWDate) -> Unit = {},
             clickDay: (HabitWDate) -> Unit = {},
             getResID: (HabitWDate, LocalDate) -> Int? = { _, _ -> null },
             listData: List<LocalDate> = arrayListOf()) {

    Card(modifier = Modifier.fillMaxWidth()
                            .height(Dp(55f))
                            .padding(bottom = 3.dp)
                            .clickable { clickCard(item) } ) {
        Row(modifier = Modifier.padding(10.dp)
                               .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically ) {

            Column(Modifier.weight(4f)) {
                Text(text = item.habitName, fontSize = 20.sp)
            }

            for (data in listData) {
                Column(modifier = Modifier.weight(1f),
                       horizontalAlignment = Alignment.CenterHorizontally) {
                    OldDaysView(data, getResID(item, data))
                }
            }

            Column(Modifier.fillMaxHeight()) {
                OutlinedButton(onClick = { clickDay(item) },
                               Modifier.fillMaxHeight()) {

                    val idResources = getResID(item, LocalDate.now()) ?: return@OutlinedButton
                    Image(
                        painter = painterResource(id = idResources),
                        contentDescription = "image",
                    )
                }
            }
        }
    }
    Divider(Modifier.fillMaxWidth(), Color.DarkGray)
}

// Template view on Swipe
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeCardView(item: HabitWDate,
                  dismissAction: (HabitWDate) -> Unit = {},
                  content: @Composable () -> Unit ) {

    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) dismissAction(item)

    SwipeToDismiss(state = dismissState,
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.White
                    else -> Color.Red
                }
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete

            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(Modifier.fillMaxSize()
                        .background(color)
                        .padding(horizontal = Dp(20f)),
                contentAlignment = alignment) {
                Icon(imageVector = icon,
                     contentDescription = "Delete Icon",
                     modifier = Modifier.scale(scale))
            }
        },
        modifier = Modifier.padding(vertical = Dp(1f)),
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
        },
        dismissContent = { content() }
    )
}

//Drawable old day
@Composable
fun OldDaysView(day: LocalDate, resourceId: Int?) {
    if (resourceId == null) return
    Column(Modifier.padding(top = 0.dp)) {
        Text(text = day.dayOfWeek.name.substring(0..2), fontSize = 7.sp)
    }
    Column(Modifier.padding(top = 10.dp)) {
        Image(painter = painterResource(id = resourceId),
              contentDescription = day.format(DateTimeFormatter.BASIC_ISO_DATE),
              modifier = Modifier.width(13.dp))
    }
}
