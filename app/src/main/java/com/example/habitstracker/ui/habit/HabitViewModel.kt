package com.example.habitstracker.ui.habit

import androidx.lifecycle.*
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.ui.common.InsertRemoveDate
import com.example.habitstracker.ui.common.interfaces.INavigation
import com.example.habitstracker.utils.LocalCalendarUtility
import com.example.habitstracker.viewmodels.NavigationHelper
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
    ViewModel(), INavigation by NavigationHelper() {
    private var _habitWDate: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var habitWDate: LiveData<HabitWDate> = _habitWDate

    private val idCalendar: Int?
        get() {
            return habitWDate.value?.calendarId
        }
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
    val listOfDates: List<LocalDate>
        get() {
            if (habitWDate.value?.listOfDates != null)
                return habitWDate.value?.listOfDates!!
            return emptyList()
        }

    fun setHabitName(name: String) {
        if (habitWDate.value?.habitId != null) {
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

    fun navigateToEditDates() {
        navToEditDatesDialog()
    }

    fun navigateToShowLocalCalendars() {
        navToShowLocalCalendars()
    }

    fun setItem(id: Long) {
        val stateIn = HabitsRepository.getHabitWithDates(id).distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), null)
        viewModelScope.launch {
            stateIn.collect() {
                _habitWDate.setValue(it)
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
        removeDate(idHabit, date)
    }

}