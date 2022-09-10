package com.example.habitstracker.ui.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.R
import com.example.habitstracker.data.common.Notificator
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.NotificationEntity
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.models.NotificationData
import com.example.habitstracker.ui.common.interfaces.INavigation
import com.example.habitstracker.viewmodels.NavigationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor
    (@ApplicationContext private val context: Context) : ViewModel(), INavigation by NavigationHelper() {

    fun getItem(habit_id: Long): HabitEntity? = HabitsRepository.getHabitById(habit_id)
    fun getSelectedTime(id: Long): LocalTime? {
        val entity = getItem(id)
        if (entity!=null){
            val notifyInfo = HabitsRepository.getNotificationByHabitId(id)
            if (notifyInfo != null){
                return LocalTime.ofSecondOfDay(notifyInfo.time)
        }}
        return null
    }
    fun getNotificationInfo(habit_id: Long): NotificationEntity?{
        return HabitsRepository.getNotificationByHabitId(habit_id)
    }
    fun setNotification(time: String, id: Long){
        Log.d("dLog","setNotification($time, $id)")
        val entity = getItem(id)
        val localTime = LocalTime.parse(time, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        val localDateTime:LocalDateTime
        if (localTime.isAfter(LocalTime.now())){
            localDateTime = localTime.atDate(LocalDate.now())
        }else{
            localDateTime = localTime.atDate(LocalDate.now().plusDays(1))
        }
        if (entity != null){
            Notificator(
                context,
                NotificationData(
                    id.toInt(),
                    entity.name,
                    "Hey! Have you done your habit?",
                    R.drawable.ic_notify_icon,
                    localDateTime,
                    true
                )
            ).createNotification()
            insertOrUpdateNotify(entity.id_habit!!,localTime)
        }
    }
    fun removeNotificationInfo(habit_id: Long){
        Log.d("dLog","removeNotificationInfo($habit_id)")
        HabitsRepository.removeNotification(habit_id)
    }
    fun insertOrUpdateNotify(id: Long, time: LocalTime){
        Log.d("dLog","insertOrUpdateNotify($id, $time")
        HabitsRepository.getNotificationByHabitId(id)?.let { entity ->
            viewModelScope.launch {
                val updatedEntity = entity.apply { this.time = time.toSecondOfDay().toLong() }
                HabitsRepository.updateNotification(updatedEntity)
            }
            return@insertOrUpdateNotify
        }
        val entity = NotificationEntity(null, id, time.toSecondOfDay().toLong())
        viewModelScope.launch {
            HabitsRepository.insertNotification(entity)
        }
    }
}