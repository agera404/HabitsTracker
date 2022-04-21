package com.example.habitstracker.repositories

import com.example.habitstracker.database.AppDatabase
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object HabitsRepository {
    lateinit var db: AppDatabase
    suspend fun insertHabit(habit: Habit, date: String) = withContext(Dispatchers.IO + NonCancellable){
        val id_habits = db.habitDao().Insert(habit)
        var date = DateWhenCompleted(null, date, id_habits)
        db.dateDao().Insert(date)
    }
    fun getAllHabits(): Flow<List<HabitWDate>> = db.dateDao().getHabitWithDateLists()


}