package com.example.habitstracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.databinding.ToolbarBinding
import com.example.habitstracker.fragments.CreateNewHabitDialogFragment
import com.example.habitstracker.repositories.HabitsRepository


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        System.out.println("abvg")
        Log.d("dLog", "init MainActivity")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        HabitsRepository.db = AppDatabase.getInstance(this)

        navHostFragment= supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController= navHostFragment.navController

        toolbarBinding = binding.toolbarInc
        toolbarBinding.addNewHabitBttn.setOnClickListener({showNewHabitDialogFragment()})
        //navController.navigate()
        //findNavController(R.id.fragment_container_view).navigate(R.id.mainFragment)
    }
    fun showNewHabitDialogFragment(){
        val fm: FragmentManager = supportFragmentManager
        val dialogFragment: CreateNewHabitDialogFragment = CreateNewHabitDialogFragment()
        dialogFragment.show(fm,"create new habit")
    }


}