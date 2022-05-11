package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(date: DateWhenCompleted)

    @Update
    abstract suspend fun update(date: DateWhenCompleted)

    @Delete
    abstract fun delete(date: DateWhenCompleted)

    @Query("SELECT * FROM DateWhenCompleted WHERE date = :mdate AND id_habit = :idhabit")
    fun findDateByDate(mdate:String, idhabit: Long): List<DateWhenCompleted>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabitWithDateLists(): Flow<List<HabitWDate>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id_habit = :idhabit")
    fun getHabitWithDateById(idhabit: Long): Flow<HabitWDate>


}