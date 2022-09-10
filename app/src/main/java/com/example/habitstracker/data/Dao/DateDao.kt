package com.example.habitstracker.data.Dao

import androidx.room.*
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: DateEntity)

    @Update
    abstract suspend fun update(date: DateEntity)

    @Delete
    abstract fun delete(date: DateEntity)

    @Query("SELECT * FROM DateEntity WHERE date = :date AND id_habit = :idhabit")
    fun getDateEntity(date:String, idhabit: Long): DateEntity?

    @Query("SELECT * FROM DateEntity WHERE date = :date AND id_habit = :idhabit")
    fun findDateByDate(date:String, idhabit: Long): List<DateEntity>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabitWithDateLists(): Flow<List<HabitWDate>>

    @Transaction
    @Query("SELECT * FROM habits WHERE id_habit = :idhabit")
    fun getHabitWithDateById(idhabit: Long): Flow<HabitWDate>
}