package com.example.habitstracker.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.EventObserver
import com.example.habitstracker.R
import com.example.habitstracker.ui.adapters.HabitsAdapter
import com.example.habitstracker.ui.adapters.SwipeToDeleteCallback
import com.example.habitstracker.databinding.MainFragmentBinding
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.ui.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    var adapter: HabitsAdapter? = null

    private val viewModel: MainViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun observeData(){
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver { destination_id ->
            if(findNavController().currentDestination?.id == R.id.mainFragment)
                findNavController().navigate(destination_id as Int)
        })
        viewModel.habitsList.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Log.d("dLog", "observer: "+ it.size)
                initAdapter(it as MutableList<HabitWDate>)
            }
        })
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            activityViewModel.setItemId(it.habitId!!)
        })
    }
    private fun initAdapter(list: MutableList<HabitWDate>){
        adapter = HabitsAdapter(list,
            isDateExist = { id: Long, date: LocalDate -> viewModel.isDateExist(id,date)},
            insertToday = { habitEntity: HabitEntity -> viewModel.insertTodayDate(habitEntity)},
            removeToday = { dateEntity: DateEntity ->  viewModel.removeTodayDate(dateEntity)},
            onItemClicked = {habitWDate: HabitWDate -> viewModel.navigateToHabitFragment(habitWDate)},
            removeHabit = {habit: HabitEntity -> viewModel.deleteHabit(habit)})
        recyclerView?.adapter = adapter
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter!!.removeAt(viewHolder.bindingAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }
}