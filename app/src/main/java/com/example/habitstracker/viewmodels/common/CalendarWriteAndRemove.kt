package com.example.habitstracker.viewmodels.common

import android.content.Context
import android.util.Log
import com.example.habitstracker.common.LocalCalendarUtility
import com.example.habitstracker.models.EventEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.repositories.HabitsRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

interface ICalendarWriteAndRemove{
    suspend fun writeToCalendar(context: Context, habitEntity: HabitEntity, date: LocalDate)
    fun removeFromCalendar(context: Context, idEvent: Long)
}
class CalendarWriteAndRemove: ICalendarWriteAndRemove {
    override suspend fun writeToCalendar(context: Context, habitEntity: HabitEntity, date: LocalDate) {
        if (habitEntity.calendar_id != null){
            val calendar: Calendar = Calendar.getInstance()
            val zoneId: ZoneId = ZoneId.systemDefault()
            calendar.set(date.year, date.month.value - 1, date.dayOfMonth)
            Log.d("dLog", "Year: "+ date.year + " month: " + (date.month.value - 1) + " day: "+ date.dayOfMonth)
            val event = LocalCalendarUtility(context).createEvent(habitEntity.calendar_id!!, calendar, habitEntity.name)
            val eventId = LocalCalendarUtility(context).addEventToCalendar(event)
            if (eventId != null){
                HabitsRepository.insertEvent(EventEntity(null, eventId, habitEntity.id_habit!!, habitEntity.calendar_id!!, date))
            }
        }else{
        }
    }

    override fun removeFromCalendar(context: Context, idEvent: Long) {
        LocalCalendarUtility(context).removeEvent(idEvent)
        val eventEntity = HabitsRepository.getEventEntityById(idEvent)
        HabitsRepository.removeEvent(eventEntity)
    }

}