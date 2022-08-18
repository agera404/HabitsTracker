package com.example.habitstracker.ui.main



import androidx.lifecycle.*
import com.example.habitstracker.models.*
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.viewmodels.NavigationHelper
import com.example.habitstracker.ui.common.InsertRemoveDate
import com.example.habitstracker.ui.common.interfaces.INavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val ird: InsertRemoveDate) :
    ViewModel(), INavigation by NavigationHelper()
    {
    private var _habitsList: MutableLiveData<List<HabitWDate>> = MutableLiveData<List<HabitWDate>>()
    val habitsList: LiveData<List<HabitWDate>> = _habitsList

    private var _selectedItem: MutableLiveData<HabitWDate> = MutableLiveData<HabitWDate>()
    var selectedItem: LiveData<HabitWDate> = _selectedItem

    init {
        getHabitsList()
    }

    private fun getHabitsList() {
        val stateIn = HabitsRepository.getAllHabits().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000),null)
        viewModelScope.launch{
            _habitsList = stateIn.collect {
                _habitsList.postValue(it)
            }
        }
    }

    fun isDateExist(idhabit: Long, date: LocalDate): Boolean {
        val habitWDate = habitsList.value!!.first{it.habitId == idhabit}
        if (habitWDate.getDateEntityByDate(date) == null){
            return false
        }
        return true
    }
    fun insertTodayDate(habitEntity: HabitEntity) {
        viewModelScope.launch {
            if (habitEntity.id_habit != null){
                ird.insertDate(habitEntity.id_habit, LocalDate.now())
            }
        }
    }

    fun removeTodayDate(date: DateEntity) {
        HabitsRepository.removeDate(date)
        ird.removeDate(date.id_habit, date.date)
    }

    fun deleteHabit(habitEntity: HabitEntity) {
        HabitsRepository.deleteHabit(habitEntity)
    }

    fun navigateToHabitFragment(habitWDate: HabitWDate) {
        navToHabitFragment()
        _selectedItem.value = habitWDate
    }
}