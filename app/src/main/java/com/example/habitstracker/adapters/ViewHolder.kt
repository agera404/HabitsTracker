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

class ViewHolder(private val _binding: HabitItemBinding): RecyclerView.ViewHolder(_binding.root){

    fun bind(item: HabitWDate, onCheckButton:(Habit)->Boolean, insertToday:(Habit)->Unit, deleteToday:(DateWhenCompleted)->Unit){
        _binding.habitItemTextView.text = item.habit.name
        setBackgroundForImageButton(onCheckButton(item.habit), _binding.habitItemBttn)
        _binding.habitItemBttn.setOnClickListener { view->
            onClickImageButton(view, item, onCheckButton, insertToday, deleteToday)
        }
        checkDate(getDaysAgo(1), item.dates).also { flag ->
            setBackgroundForImageView( _binding.habitItemYesterday, flag)
        }
        checkDate(getDaysAgo(2), item.dates).also { flag ->
            setBackgroundForImageView( _binding.habitItemTwodayago, flag)
        }
        checkDate(getDaysAgo(3), item.dates).also { flag ->
            setBackgroundForImageView( _binding.habitItemThreedayago, flag)
        }
    }
    private fun onClickImageButton(view: View, item: HabitWDate, onCheckButton: (Habit) -> Boolean, insertToday: (Habit) -> Unit, deleteToday: (DateWhenCompleted) -> Unit){
        view.setTag(""+item.habit.id_habit)
        if(!onCheckButton(item.habit)){
            insertToday(item.habit)
        }else{
            for (date in item.dates){
                if (DateConverter.dateToString(date.date) == DateConverter.dateToString(Date()) &&
                    date.habit == item.habit.id_habit) {
                    deleteToday(date)
                    Log.d("dLog", "delete " + DateConverter.dateToString(date.date) + " for "+ item.habit.name)
                }
            }
        }
    }
    private fun setBackgroundForImageButton(flag:Boolean, view: View){
        (view as ImageButton).also { imageButton ->
            if (flag){
                imageButton.setBackgroundResource(R.drawable.ic_baseline_done_24)
            }else{
                imageButton.setBackgroundResource(R.drawable.ic_baseline_close_24)
            }
        }

    }
    private fun checkDate(day: Date, dates: List<DateWhenCompleted>): Boolean{
        for (date in dates){
            if (date.date == day) return true
        }
        return false
    }
    private fun setBackgroundForImageView(view: View, flag:Boolean){
        (view as ImageView).also { imageView ->
            if (flag){
                imageView.setBackgroundColor(R.drawable.ic_baseline_done_24)
            }else{
                imageView.setBackgroundColor(R.drawable.ic_baseline_close_24)
            }
        }
    }
    private fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }
}