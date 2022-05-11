package com.example.habitstracker.repositories

import android.util.Log
import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.*

object HabitsRepository {
    lateinit var db: AppDatabase
    suspend fun insertDate(idHabit: Long, date: Date) = withContext(Dispatchers.IO + NonCancellable){
        var _date = DateWhenCompleted(null, date, idHabit)
        db.dateDao().insert(_date)
    }
    suspend fun insertHabit(habit: Habit) = withContext(Dispatchers.IO + NonCancellable){
        db.habitDao().insert(habit)
    }
    fun deleteHabit(habit: Habit){
        db.habitDao().delete(habit)
    }
    fun isHabitExist(habit: Habit): Boolean{
        if (habit.id_habit != null){
            val habit = db.habitDao().findHabitById(habit.id_habit)
            if (habit!= null) return true
        }
        return false
    }
    fun removeDate(date: DateWhenCompleted){
        db.dateDao().delete(date)
    }
    fun getAllHabits(): Flow<List<HabitWDate>> = db.dateDao().getHabitWithDateLists()
    fun getHabitWithDates(idHabit: Long): Flow<HabitWDate> = db.dateDao().getHabitWithDateById(idHabit)

    fun isDateExist(date: Date, idHabit: Long):Boolean{
        val result = DateConverter.dateToString(date)?.let { db.dateDao().findDateByDate(it, idHabit) }
        if (result != null && result.size > 0) return true
        return false
    }
    fun findDate(date: Date, idHabit: Long):Long?{
        val result = DateConverter.dateToString(date)?.let { db.dateDao().findDateByDate(it, idHabit) }
        if (result != null && result.size > 0) return result.first().id_date
        return null
    }


}