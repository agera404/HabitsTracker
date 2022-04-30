package com.example.habitstracker.repositories

import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

object HabitsRepository {
    lateinit var db: AppDatabase
    suspend fun insertDate(idHabit: Long, date: Date) = withContext(Dispatchers.IO + NonCancellable){
        var _date = DateWhenCompleted(null, date, idHabit)

        db.dateDao().Insert(_date)
    }
    suspend fun insertHabit(habit: Habit) = withContext(Dispatchers.IO + NonCancellable){
        db.habitDao().Insert(habit)
    }
    fun deleteHabit(habit: Habit){
        db.habitDao().Delete(habit)
    }
    fun isHabitExist(habit: Habit): Boolean{
        if (habit.id_habit != null){
            val habit = db.habitDao().FindHabitById(habit.id_habit)
            if (habit!= null) return true
        }
        return false
    }
    fun deleteDate(date: DateWhenCompleted){
        db.dateDao().Delete(date)
    }
    fun getAllHabits(): Flow<List<HabitWDate>> = db.dateDao().getHabitWithDateLists()


}