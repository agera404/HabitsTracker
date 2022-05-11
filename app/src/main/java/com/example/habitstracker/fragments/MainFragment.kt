package com.example.habitstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.EventObserver
import com.example.habitstracker.MainActivity
import com.example.habitstracker.adapters.HabitsAdapter
import com.example.habitstracker.adapters.SwipeToDeleteCallback
import com.example.habitstracker.databinding.MainFragmentBinding
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import com.example.habitstracker.viewmodels.MainViewModel


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    var adapter: HabitsAdapter? = null

    private lateinit var viewModel: MainViewModel
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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        initRecyclerView()
        observeData()


    }

    override fun onPause() {
        super.onPause()
        Log.d("dLog","MainFragment onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun observeData(){
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(it as Int)
        })
        viewModel.habitsList.observeForever {
            initAdapter(it as MutableList<HabitWDate>)
        }
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            Log.d("dLog", "selectedItem: "+ it.habit.name)
            (activity as MainActivity).viewModel.setItem(it)
        })
    }
    private fun initAdapter(list: MutableList<HabitWDate>){
        adapter = HabitsAdapter(list,
            onCheckButton = {habit: Habit -> viewModel.isDateExist(habit)},
            insertToday = {habit: Habit -> viewModel.insertTodayDate(habit)},
            removeToday = { dateWhenCompleted: DateWhenCompleted ->  viewModel.removeTodayDate(dateWhenCompleted)},
            onItemClicked = {habitWDate: HabitWDate -> viewModel.navigateToHabitFragment(habitWDate)})
        recyclerView?.adapter = adapter
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteHabit(list[pos].habit)
                adapter!!.notifyItemRemoved(pos)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            //addItemDecoration(SimpleDividerItemDecoration(this))
        }
    }
}