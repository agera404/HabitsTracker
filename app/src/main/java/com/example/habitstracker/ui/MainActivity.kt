package com.example.habitstracker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.habitstracker.data.database.AppDatabase
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.ui.main.MainActivityScreen
import com.example.habitstracker.ui.theme.AppTheme
import com.example.habitstracker.ui.theme.DarkColors
import com.example.habitstracker.ui.theme.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            AppTheme() {
                MainActivityScreen()
            }
        }
        HabitsRepository.db = AppDatabase.getInstance(this.applicationContext)

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




}