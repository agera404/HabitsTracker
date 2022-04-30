package com.example.habitstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.R
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun observeData(){
        viewModel.habitsList.observeForever {
            initAdapter(it as MutableList<HabitWDate>)
        }
    }
    private fun initAdapter(list: MutableList<HabitWDate>){
        adapter = HabitsAdapter(list,
            onCheckButton = {habit: Habit -> viewModel.checkDate(habit)},
            insertToday = {habit: Habit ->viewModel.insertTodayDate(habit)},
            deleteToday = {dateWhenCompleted: DateWhenCompleted ->  viewModel.deleteTodayDate(dateWhenCompleted)})
        recyclerView?.adapter = adapter
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                Log.d("dLog","Trying to delete "+ list[pos].habit.name)
                viewModel.deleteHabit(list[pos].habit)
                adapter!!.notifyItemRemoved(pos)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    fun onClick(){
        findNavController().navigate(R.id.blankFragment)
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            //addItemDecoration(SimpleDividerItemDecoration(this))
        }
    }
}