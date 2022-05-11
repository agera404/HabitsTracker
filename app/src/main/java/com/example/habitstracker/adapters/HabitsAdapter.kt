package com.example.habitstracker.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.HabitItemBinding
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate


class HabitsAdapter(private val dataSet: List<HabitWDate>,
                    val onCheckButton:(Habit)->Boolean,
                    val insertToday:(Habit)->Unit,
                    val removeToday:(DateWhenCompleted)->Unit,
                    val onItemClicked:(habitWDate: HabitWDate)->Unit) : RecyclerView.Adapter<ViewHolder>() {

    private var habitsSet: List<HabitWDate> = listOf()
        get() {
            if (field.isNotEmpty()) return field
            return listOf()
        }
        set(value){
            field = value
            notifyDataSetChanged()

        }
    init {
        habitsSet = dataSet
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habitsSet[position]
        holder.bind(habit, onCheckButton, insertToday, removeToday)
        holder.itemView.setOnClickListener {
            onItemClicked(habit)
        }
    }

    override fun getItemCount(): Int {
        return habitsSet.size
    }

}