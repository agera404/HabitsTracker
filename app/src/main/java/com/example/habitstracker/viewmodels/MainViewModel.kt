package com.example.habitstracker.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.repositories.HabitsRepository
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    var _habitsList: MutableLiveData <List<HabitWDate>> = MutableLiveData <List<HabitWDate>>()
    val habitsList: LiveData <List<HabitWDate>>
        get() = _habitsList
    init {
        getHabitsList()
    }
    fun getHabitsList()
    {
        viewModelScope.launch{
            //HabitsRepository.insertHabit(Habit(null,"Habit 1"), SimpleDateFormat("dd/M/yyyy").format(Date()))
            HabitsRepository.getAllHabits().collect(){ list ->
                Log.d("dLog","list size "+list.size)
                _habitsList.value = list
            }
        }
    }

}