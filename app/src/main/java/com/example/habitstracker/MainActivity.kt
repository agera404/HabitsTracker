package com.example.habitstracker

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.fragments.MainFragment
import com.example.habitstracker.repositories.HabitsRepository


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        System.out.println("abvg")
        Log.d("dLog", "init MainActivity")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        HabitsRepository.db = AppDatabase.getInstance(this)

        //val navHostFragment= supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        //val navController= navHostFragment.navController
       //navController.navigate()
        //findNavController(R.id.fragment_container_view).navigate(R.id.mainFragment)
        //val navOptions: NavOptions = NavOptions.Builder()
            //.setPopUpTo(R.id.main_fragment, true)
            //.build()

    }
}