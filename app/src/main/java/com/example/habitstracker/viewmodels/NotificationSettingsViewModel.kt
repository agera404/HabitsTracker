package com.example.habitstracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.NotificationEntity
import com.example.habitstracker.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.common.interfaces.INavigationVM
import kotlinx.coroutines.launch
import java.time.LocalTime

class NotificationSettingsViewModel : ViewModel(), INavigationVM by NavigationVM() {

    fun navigateToTimePicker(){
        this.navToTimePickerDialog()
    }
    fun getItem(habit_id: Long): HabitEntity? = HabitsRepository.getHabitById(habit_id)
    fun getNotificationInfo(habit_id: Long): NotificationEntity?{
        return HabitsRepository.getNotificationByHabitId(habit_id)
    }
    fun removeNotificationInfo(habit_id: Long){
        HabitsRepository.removeNotification(habit_id)
    }
    fun insertOrUpdateNotify(id: Long, time: LocalTime){
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