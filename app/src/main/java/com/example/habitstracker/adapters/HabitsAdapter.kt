package com.example.habitstracker.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.HabitItemBinding
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import java.time.LocalDate


class HabitsAdapter(private val dataSet: List<HabitWDate>,
                    private val isDateExist: (id: Long, date: LocalDate) -> Boolean,
                    private val insertToday:(HabitEntity)->Unit,
                    private val removeToday:(DateEntity)->Unit,
                    private val onItemClicked:(habitWDate: HabitWDate)->Unit,
                    private val removeHabit:(habit: HabitEntity)->Unit) : RecyclerView.Adapter<ViewHolder>() {

    private var habitsSet: MutableList<HabitWDate> = mutableListOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    init {
        habitsSet = dataSet as MutableList<HabitWDate>
    }
    fun removeAt(index: Int){
        removeHabit(habitsSet[index].habitEntity)
        habitsSet.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val _binding = HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habitsSet[position]
        holder.bind(habit, isDateExist, insertToday, removeToday)
        holder.itemView.setOnClickListener {
            onItemClicked(habit)
        }
    }

    override fun getItemCount(): Int {
        return habitsSet.size
    }

}