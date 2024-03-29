package com.example.habitstracker.ui.main


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.habitstracker.models.*
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.ui.common.InsertRemoveDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val ird: InsertRemoveDate) :
    ViewModel() {

    var habitsListState = mutableStateListOf<HabitWDate>()

    init {
        getHabitsList()
    }

    fun getPastThreeDays(): List<LocalDate> {
        val today = LocalDate.now()
        return listOf<LocalDate>(
            today.minusDays(3),
            today.minusDays(2),
            today.minusDays(1)
        )
    }

    private fun getHabitsList() {
        val stateIn = HabitsRepository.getAllHabits().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000), null
        )
        viewModelScope.launch {
            //_habitsList =
                stateIn.collect { list ->
                if (list != null) {
                    habitsListState.clear()
                    habitsListState.addAll(list)

                }
            }
        }
    }

    fun isDateExist(idhabit: Long, date: LocalDate): Boolean? {
        if (habitsListState.size > 0){
            val habitWDate = habitsListState.firstOrNull { it.habitId == idhabit }
            if (habitWDate != null) {
                if (habitWDate.getDateEntityByDate(date) == null) {
                    return false
                }
                return true
            }
        }
        return null
    }

    fun insertTodayDate(habitEntity: HabitEntity) {
        viewModelScope.launch {
            if (habitEntity.id_habit != null) {
                ird.insertDate(habitEntity.id_habit, LocalDate.now())
            }
        }
    }

    fun removeTodayDate(idhabit: Long) {
        val dateEntity = HabitsRepository.getDateEntity(LocalDate.now(),idhabit)
        if (dateEntity != null) {
            HabitsRepository.removeDate(dateEntity)
            ird.removeDate(idhabit, LocalDate.now())
        }
    }

    fun deleteHabit(habitEntity: HabitEntity) {
        HabitsRepository.deleteHabit(habitEntity)
    }

}