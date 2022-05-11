package com.example.habitstracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.databinding.ToolbarBinding
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.ActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var viewModel: ActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(com.example.habitstracker.viewmodels.ActivityViewModel::class.java)

        viewModel.navigateEvent.observe(this, EventObserver {
            navController.navigate(it as Int)
        })

        HabitsRepository.db = AppDatabase.getInstance(this)

        navHostFragment= supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController= navHostFragment.navController

        toolbarBinding = binding.toolbarInc
        toolbarBinding.addNewHabitBttn.setOnClickListener { viewModel.navigateToCreateNewHabit() }
    }

    var doubleBackToExitPressedOnce = false;
    override fun onBackPressed() {
        navController.popBackStack()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}