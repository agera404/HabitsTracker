package com.example.habitstracker.ui.editdates

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.data.repositories.HabitsRepository
import com.example.habitstracker.ui.common.InsertRemoveDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
@HiltViewModel
class EditDatesViewModel @Inject constructor(private val ird: InsertRemoveDate,) : ViewModel()
    {
    var idHabit: Long = 0

    private var _listOfDates = mutableStateOf<List<LocalDate>>(listOf())
    val listOfDates = _listOfDates

    fun init(idHabit: Long){
        this.idHabit = idHabit
        setItem(idHabit)
    }

    private fun setItem(id: Long) {
        val stateIn = HabitsRepository.getHabitWithDates(id).distinctUntilChanged().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000),null)
        viewModelScope.launch {
            stateIn.collect(){
                if (it != null){
                    _listOfDates.value = it.listOfDates
                }
            }
        }
    }

    fun changeDataStatus(date: LocalDate){
        if (isDateExist(idHabit, date)){
            removeDate(idHabit, date)
        }else{
            insertDate(idHabit, date)
            setItem(idHabit)
        }
    }

    private fun isDateExist(idHabit: Long, date: LocalDate):Boolean{
        return ird.isDateExist(idHabit, date)
    }
    private fun insertDate(idHabit: Long, date: LocalDate){

        viewModelScope.launch {
            ird.insertDate(idHabit, date)
        }
    }
    private fun removeDate(idHabit: Long, date: LocalDate){
        ird.removeDate(idHabit, date)
    }
}