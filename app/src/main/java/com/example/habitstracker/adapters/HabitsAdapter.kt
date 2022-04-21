package com.example.habitstracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.HabitItemBinding

import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class HabitsAdapter(private val dataSet: List<HabitWDate>, val onClick:()->Unit) : RecyclerView.Adapter<HabitsAdapter.ViewHolder>() {
    class ViewHolder(private val _binding: HabitItemBinding): RecyclerView.ViewHolder(_binding.root){
        fun bind(habit: HabitWDate, onClick:()->Unit){
            _binding.habitItemTextView.text = habit.habit.name
            _binding.habitItemBttn.setOnClickListener { view->
                onClick() }
            Log.d("dLog", "bind name: "+ habit.habit.name)
        }
    }
    private var habitsSet: List<HabitWDate> = listOf()
        get() {
            if (field.isNotEmpty())return field
            return listOf()
        }
        set(value){
            field = value
            notifyDataSetChanged()
            Log.d("dLog","set dataset in adapter")
            Log.d("dLog","size dataset in adapter: "+ field.size)
        }
    init {
        Log.d("dLog","init adapter")
        habitsSet = dataSet
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("dLog","onCreateViewHolder")
        val _binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("dLog","onBindViewHolder")
        val habit = habitsSet[position]
        holder.bind(habit, onClick)
    }
    override fun getItemCount(): Int {
        Log.d("dLog","habitsSet.size "+habitsSet.size)
        return habitsSet.size
    }

}