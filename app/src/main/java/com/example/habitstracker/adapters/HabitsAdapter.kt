package com.example.habitstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.HabitItemBinding

import com.example.habitstracker.models.Habit

class HabitsAdapter(private val habitsSet: Array<Habit>) : RecyclerView.Adapter<HabitsAdapter.ViewHolder>() {
    class ViewHolder(private val _binding: HabitItemBinding): RecyclerView.ViewHolder(_binding.root){
        fun bind(habit: Habit){
            _binding.habitItemTextView.text = habit.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habitsSet[position]
        holder.bind(habit)
    }
    override fun getItemCount(): Int = habitsSet.size

}