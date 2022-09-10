package com.example.habitstracker.ui.common

import android.content.Context
import android.util.Log
import com.example.habitstracker.utils.LocalCalendarUtility
import com.example.habitstracker.models.EventEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.data.repositories.HabitsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


class CalendarWriteAndRemove @Inject constructor
    (@ApplicationContext private val context: Context) {
    suspend fun writeToCalendar(habitEntity: HabitEntity, date: LocalDate) {
        if (habitEntity.calendar_id != null){
            val calendar: Calendar = Calendar.getInstance()
            //val zoneId: ZoneId = ZoneId.systemDefault()
            calendar.set(date.year, date.month.value - 1, date.dayOfMonth)
            val event = LocalCalendarUtility(context).createEvent(habitEntity.calendar_id!!, calendar, habitEntity.name)
            val eventId = LocalCalendarUtility(context).addEventToCalendar(event)
            if (eventId != null){
                HabitsRepository.insertEvent(EventEntity(null, eventId, habitEntity.id_habit!!, habitEntity.calendar_id!!, date))
            }
        }else{
        }
    }

    fun removeFromCalendar(idEvent: Long) {
        LocalCalendarUtility(context).removeEvent(idEvent)
        val eventEntity = HabitsRepository.getEventEntityById(idEvent)
        HabitsRepository.removeEvent(eventEntity)
    }

}