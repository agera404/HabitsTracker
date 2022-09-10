package com.example.habitstracker.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.habitstracker.ui.navigation.Screens
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Main(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val habits = viewModel.habitsListState

    Scaffold(topBar = { MainToolBar(navController = navController) }, content = {
        Surface(
            modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
        ) {
            LazyColumn() {
                items(habits) { item ->
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        viewModel.deleteHabit(item.habitEntity)
                        habits.remove(item)
                    }
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

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = Dp(20f)),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        }, modifier = Modifier
                            .padding(vertical = Dp(1f)),
                        directions = setOf(
                            DismissDirection.EndToStart
                        ),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                        }) {
                        Card(
                            elevation = animateDpAsState(
                                if (dismissState.dismissDirection != null) 4.dp else 0.dp
                            ).value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(Dp(55f))
                                .align(alignment = Alignment.CenterVertically).padding(bottom = 3.dp)
                                .clickable { navController.navigate(Screens.Habit.withArg(item.habitId.toString())) }
                        ){
                            Row(
                                Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(4f)) {
                                    Text(text = item.habitName, fontSize = 20.sp)
                                }
                                for (day in viewModel.getPastThreeDays()) {
                                    Column(
                                        Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        Column(Modifier.padding(top = 0.dp)) {
                                            Text(text = day.dayOfWeek.name.substring(0..2), fontSize = 7.sp)
                                        }
                                        Column(Modifier.padding(top = 10.dp)) {
                                            val id = if (viewModel.isDateExist(
                                                    item.habitId!!,
                                                    day
                                                )
                                            ) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
                                            Image(
                                                painter = painterResource(id = id),
                                                contentDescription = day.format(DateTimeFormatter.BASIC_ISO_DATE),
                                                Modifier.width(13.dp)
                                            )
                                        }

                                    }
                                }
                                Column(
                                    Modifier
                                        .fillMaxHeight()
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            if (viewModel.isDateExist(item.habitId!!, LocalDate.now())) {
                                                viewModel.removeTodayDate(item.habitId!!)
                                            } else {
                                                viewModel.insertTodayDate(item.habitEntity)
                                            }
                                        },
                                        Modifier.fillMaxHeight()
                                    ) {
                                        val id = if (viewModel.isDateExist(
                                                item.habitId!!,
                                                LocalDate.now()
                                            )
                                        ) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
                                        Image(
                                            painter = painterResource(id = id),
                                            contentDescription = "image",
                                        )
                                    }

                                }
                            }
                        }
                        Divider(Modifier.fillMaxWidth(), Color.DarkGray)

                    }

                }
            }
        }
    })

}

