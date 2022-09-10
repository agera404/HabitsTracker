package com.example.habitstracker.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.habitstracker.R
import com.example.habitstracker.ui.NavGraph
import com.example.habitstracker.ui.navigation.Screens

@Composable
fun MainActivityScreen(){
    val navController = rememberNavController()
    Scaffold(
    content = { it ->
        Column(Modifier.padding(it)) {
            Row() {
                NavGraph(navController = navController)
            }
        }
    })

}

@Composable
fun MainToolBar(navController: NavController){
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name))},
        actions = {
            IconButton(onClick = { navController.navigate(Screens.CreateNewHabit.route) }) {
                Icon(Icons.Filled.Add, contentDescription = "add new habit")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Settings, contentDescription = "settings")
            }
        })
}

@Composable
@Preview
private fun MainActivityScreenPreview(){
    MainActivityScreen()
}