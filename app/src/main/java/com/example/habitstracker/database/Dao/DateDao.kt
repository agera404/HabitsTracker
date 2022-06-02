package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(date: DateEntity)

    @Update
    abstract suspend fun update(date: DateEntity)

    @Delete
    abstract fun delete(date: DateEntity)

    @Query("SELECT * FROM DateEntity WHERE date = :mdate AND id_habit = :idhabit")
    fun findDateByDate(mdate:String, idhabit: Long): List<DateEntity>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabitWithDateLists(): Flow<List<HabitWDate>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id_habit = :idhabit")
    fun getHabitWithDateById(idhabit: Long): Flow<HabitWDate>
}