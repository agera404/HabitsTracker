package com.example.habitstracker.repositories

import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

object HabitsRepository {
    lateinit var db: AppDatabase
    suspend fun insertDate(idHabit: Long, date: LocalDate) = withContext(Dispatchers.IO + NonCancellable){
        var _date = DateEntity(null, date, idHabit)
        db.dateDao().insert(_date)
    }
    suspend fun insertHabit(habitEntity: HabitEntity) = withContext(Dispatchers.IO + NonCancellable){
        db.habitDao().insert(habitEntity)
    }
    fun deleteHabit(habitEntity: HabitEntity){
        db.habitDao().delete(habitEntity)
    }
    fun getHabitById(id: Long): HabitEntity?{
        return db.habitDao().findHabitById(id)
    }
    suspend fun updateHabit(habitEntity: HabitEntity) = db.habitDao().update(habitEntity)
    fun isHabitExist(habitEntity: HabitEntity): Boolean{
        if (habitEntity.id_habit != null){
            val habit = db.habitDao().findHabitById(habitEntity.id_habit)
            if (habit!= null) return true
        }
        return false
    }
    fun removeDate(date: DateEntity){
        db.dateDao().delete(date)
    }
    fun getAllHabits(): Flow<List<HabitWDate>> = db.dateDao().getHabitWithDateLists()
    fun getHabitWithDates(idHabit: Long): Flow<HabitWDate> = db.dateDao().getHabitWithDateById(idHabit)

    fun isDateExist(date: LocalDate, idHabit: Long):Boolean{
        val result = DateConverter.toString(date)?.let { db.dateDao().findDateByDate(it, idHabit) }
        if (result != null && result.size > 0) return true
        return false
    }
    fun findDate(date: LocalDate, idHabit: Long):Long?{
        val result = DateConverter.toString(date)?.let { db.dateDao().findDateByDate(it, idHabit) }
        if (result != null && result.size > 0) return result.first().id_date
        return null
    }

    fun getNotificationByHabitId(idHabit: Long): NotificationEntity?{
        db.notificationDao().getEntityByHabitID(idHabit)?.let { return it }
        return null
    }
    fun removeNotification(idHabit: Long){
        val entity = getNotificationByHabitId(idHabit)
        if (entity != null) {
            db.notificationDao().delete(entity)
        }
    }
    suspend fun insertNotification(entity: NotificationEntity) = db.notificationDao().insert(entity)
    suspend fun updateNotification(entity: NotificationEntity) = db.notificationDao().update(entity)
    suspend fun insertEvent(entity: EventEntity) = db.eventDao().insert(entity)
    fun removeEvent(entity: EventEntity) = db.eventDao().delete(entity)
    fun getEventEntityById(id: Long): EventEntity = db.eventDao().getEventById(id)
    fun getEventEntityByParameters(idCalendar: Int?, idHabit: Long, date: String): EventEntity =
        db.eventDao().getEventByHabitIdCalendarIdAndDate(idCalendar,idHabit,date)

}