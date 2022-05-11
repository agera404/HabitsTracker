package com.example.habitstracker.fragments

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.EventObserver
import com.example.habitstracker.MainActivity
import com.example.habitstracker.R
import com.example.habitstracker.databinding.EditDatesDialogFragmentBinding
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.viewmodels.EditDatesDialogViewModel

class EditDatesDialogFragment : DialogFragment() {

    private var _binding: EditDatesDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: EditDatesDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditDatesDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as MainActivity).viewModel.setEditDateState(false)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditDatesDialogViewModel::class.java)
        viewModel.idHabit = (activity as MainActivity).viewModel.getItem()?.habit?.id_habit!!
        (activity as MainActivity).viewModel.setEditDateState(true)
        observeDate()
        setOnClickListeners()
    }

    private fun observeDate(){
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it as Int)
        })

        (activity as MainActivity).viewModel.selectedDate.observe(requireActivity(), Observer {
            viewModel.setSelectedDate(it)
        })
        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { date ->
            binding.selectedDate.text = DateConverter.dateToString(date)
            setBackgroundForImageButton(viewModel.isDateExist(date, viewModel.idHabit))
        })
    }

    private fun setBackgroundForImageButton(flag: Boolean){
        val drawable = if (flag) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        binding.checkDateButton.setBackgroundResource(drawable)
    }

    private fun setOnClickListeners(){
        binding.selectDateButton.setOnClickListener { viewModel.navigateToDatePicker() }
        binding.checkDateButton.setOnClickListener {
            if (viewModel.isDateExist(viewModel.selectedDate.value!!, viewModel.idHabit)){
                viewModel.removeDate(viewModel.idHabit, viewModel.selectedDate.value!!)
                setBackgroundForImageButton(false)
            }else{
                viewModel.insertDate(viewModel.idHabit, viewModel.selectedDate.value!!)
                setBackgroundForImageButton(true)
            }
        }
    }
}