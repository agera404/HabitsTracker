package com.example.habitstracker.database.Dao

import androidx.room.*
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun Insert(date: DateWhenCompleted)

    @Update
    abstract suspend fun Update(date: DateWhenCompleted)

    @Delete
    abstract fun Delete(date: DateWhenCompleted)



    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabitWithDateLists(): Flow<List<HabitWDate>>

}