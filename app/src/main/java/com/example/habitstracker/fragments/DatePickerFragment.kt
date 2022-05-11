package com.example.habitstracker.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitstracker.MainActivity
import com.example.habitstracker.models.DateConverter
import java.util.*

class DatePickerFragment :  DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerFragment = DatePickerDialog(requireContext(), this, year, month, day)
        return  datePickerFragment.apply { datePicker.maxDate = calendar.timeInMillis }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        (activity as MainActivity).viewModel.setSelectedDate(calendar.time)
    }


}