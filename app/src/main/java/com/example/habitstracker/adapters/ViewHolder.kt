package com.example.habitstracker.adapters

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.R
import com.example.habitstracker.databinding.HabitItemBinding
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateWhenCompleted
import com.example.habitstracker.models.Habit
import com.example.habitstracker.models.HabitWDate
import java.util.*

class ViewHolder(private val _binding: HabitItemBinding) : RecyclerView.ViewHolder(_binding.root) {

    fun bind(
        item: HabitWDate, onCheckButton: (Habit) -> Boolean,
        insertToday: (Habit) -> Unit,
        removeToday: (DateWhenCompleted) -> Unit
    ) {
        _binding.habitItemTextView.text = item.habit.name

        setBackgroundForImageButton(onCheckButton(item.habit), _binding.habitItemBttn)
        _binding.habitItemBttn.setOnClickListener { view ->
            onClickImageButton(view, item, onCheckButton, insertToday, removeToday)
        }

        val listOfImageView: List<Pair<Int, ImageView>> = listOf(
            Pair(1, _binding.habitItemYesterday),
            Pair(2, _binding.habitItemTwodayago),
            Pair(3, _binding.habitItemThreedayago)
        )
        for (pair in listOfImageView) {
            isDateExist(getDaysAgo(pair.first), item.dates).also { flag ->
                setBackgroundForImageView(pair.second, flag)
            }
            Log.d(
                "dLog",
                "days ago: " + getDaysAgo(pair.first) + " is " + isDateExist(
                    getDaysAgo(pair.first),
                    item.dates
                )
            )
        }


    }

    private fun onClickImageButton(
        view: View,
        item: HabitWDate,
        onCheckButton: (Habit) -> Boolean,
        insertToday: (Habit) -> Unit,
        deleteToday: (DateWhenCompleted) -> Unit
    ) {
        view.setTag("" + item.habit.id_habit)
        if (!onCheckButton(item.habit)) {
            insertToday(item.habit)
        } else {
            val dates = item.dates.filter {
                DateConverter.dateToString(it.date) == DateConverter.dateToString(Date())
            }.filter { it.id_habit == item.habit.id_habit }
            val date = dates.first()
            deleteToday(date)
        }
    }

    private fun setBackgroundForImageButton(flag: Boolean, view: View) {
        val drawable_id =
            if (flag) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        (view as ImageButton).setBackgroundResource(drawable_id)

    }

    private fun isDateExist(day: Date, dates: List<DateWhenCompleted>): Boolean {
        return dates.stream().anyMatch { DateConverter.dateToString(it.date) == DateConverter.dateToString(day) }
    }

    private fun setBackgroundForImageView(view: ImageView, flag: Boolean) {
        val drawable_id =
            if (flag) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        view.foreground = this.itemView.resources.getDrawable(drawable_id)
        //view.setBackgroundColor(this.itemView.resources.getColor(R.color.md_theme_light_background))
    }

    private fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }
}