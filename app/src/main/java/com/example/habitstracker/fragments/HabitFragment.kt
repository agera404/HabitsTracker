package com.example.habitstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.habitstracker.EventObserver
import com.example.habitstracker.MainActivity
import com.example.habitstracker.databinding.HabitFragmentBinding
import com.example.habitstracker.ui.CalendarHeatMapView
import com.example.habitstracker.viewmodels.HabitViewModel


class HabitFragment : Fragment() {

    private lateinit var viewModel: HabitViewModel

    private var _binding: HabitFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HabitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    var isViewCreated: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        setItem()
        viewModel.subscribeHabit()
        observeData()
        setDatesMap()
        setOnClickListeners()

        isViewCreated = true
    }

    private fun setItem(){
        val item = (activity as MainActivity).viewModel.getItem()
        viewModel.setItem(item!!)
    }

    private fun setOnClickListeners(){
        binding.editDatesButton.setOnClickListener { onEditButtonClicked() }
    }
    private fun onEditButtonClicked(){
        viewModel.navigateToEditDates()
    }

    private fun observeData(){
        (activity as MainActivity).viewModel.editDateState.observe(requireActivity() , Observer<Boolean?> { flag ->
            if (flag == false && isViewCreated){
                binding.heatmap.removeAllViews()
                setDatesMap()
            }
        })
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it as Int)
        })
        viewModel.habitWDate.observe(viewLifecycleOwner, Observer {
            binding.habitName.setText(it.habit.name)
        })
    }
    private fun setDatesMap(){
        val calendarHeatMapView = CalendarHeatMapView(requireContext(), viewModel.getMinDate(), viewModel.getDates())
        binding.heatmap.addView(calendarHeatMapView)

    }
}