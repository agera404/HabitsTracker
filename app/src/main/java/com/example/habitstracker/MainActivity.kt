package com.example.habitstracker

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.habitstracker.common.MyNewIntentService
import com.example.habitstracker.common.MyReceiver
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.viewmodels.ActivityViewModel
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var viewModel: ActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)

        viewModel.navigateEvent.observe(this, EventObserver {
            navController.navigate(it as Int)
        })



        navHostFragment= supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController= navHostFragment.navController

        val toolbar = binding.toolbar
        val conf = AppBarConfiguration.Builder(navController.graph).build()
        toolbar.setupWithNavController(navController,conf)
        //toolbar.title = this.resources.getString(R.string.app_name)

        toolbar.setOnMenuItemClickListener{ menuItem ->
            when(menuItem.itemId){
                R.id.add_new_habit -> {
                    viewModel.navigateToCreateNewHabit()
                    true
                }
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }

        ignoreBatteryOptimization()

    }
    private fun ignoreBatteryOptimization() {
        val intent = Intent()
        val packN = packageName
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packN)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packN")
            startActivity(intent)
        }
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