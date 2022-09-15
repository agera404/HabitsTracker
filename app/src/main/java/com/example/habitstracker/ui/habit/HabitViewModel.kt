package com.example.habitstracker.ui.habit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.ui.common.InsertRemoveDate
import com.example.habitstracker.utils.LocalCalendarUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(private val ird: InsertRemoveDate,
                                         private val localCalendarUtility: LocalCalendarUtility) :
    ViewModel() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate

    private val idCalendar: Int?
        get() {
            return habitWDate.value?.calendarId
        }
    private var _habitName = mutableStateOf("")
    val habitName: State<String> = _habitName

    val calendarName: String?
        get() {
            if (idCalendar!=null){
                val calendar = localCalendarUtility.getLocalCalendarById(idCalendar!!)
                if (calendar!=null)
                    return calendar.name
            }
            return null
        }
    val idHabit: Long?
        get() {
            return habitWDate.value?.habitId
        }

    private var _listOfDates = mutableStateListOf<LocalDate>()
    val listOfDates = _listOfDates

    fun setHabitName(name: String) {
        if (habitWDate.value?.habitId != null && name.isNotBlank()) {
            val updatedHabit = HabitEntity(
                habitWDate.value?.habitId!!,
                name,
                habitWDate.value?.habitEntity!!.calendar_id
            )
            viewModelScope.launch {
                HabitsRepository.updateHabit(updatedHabit)
            }
        }
    }

    fun setItem(id: Long) {
        val stateIn = HabitsRepository.getHabitWithDates(id).distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), null)
        viewModelScope.launch {
            stateIn.collect() {
                if (it != null) {
                    _habitWDate.value = it
                    _habitName.value = it.habitName
                    _listOfDates.clear()
                    _listOfDates.addAll(it.listOfDates)
                }
            }
        }
    }

    fun changeDataStatus(date: LocalDate) {
        if (idHabit != null) {
            if (isDateExist(idHabit!!, date)) {
                removeDate(idHabit!!, date)

            } else {

                insertDate(idHabit!!, date)
            }
        }

    }

    private fun isDateExist(idHabit: Long, date: LocalDate, ): Boolean {

        return ird.isDateExist(idHabit, date)
    }

    private fun insertDate(idHabit: Long, date: LocalDate) {
        viewModelScope.launch {
            ird.insertDate(idHabit, date)
        }
    }

    private fun removeDate(idHabit: Long, date: LocalDate) {
        ird.removeDate(idHabit, date)
    }

}