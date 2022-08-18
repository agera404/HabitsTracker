package com.example.habitstracker.ui.adapters

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.R
import com.example.habitstracker.databinding.HabitItemBinding
import com.example.habitstracker.models.DateConverter
import com.example.habitstracker.models.DateEntity
import com.example.habitstracker.models.HabitEntity
import com.example.habitstracker.models.HabitWDate
import java.time.LocalDate

class HabitViewHolder(private val _binding: HabitItemBinding) : RecyclerView.ViewHolder(_binding.root) {

    fun bind(
        item: HabitWDate, isDateExist: (id: Long, date: LocalDate) -> Boolean,
        insertToday: (HabitEntity) -> Unit,
        removeToday: (DateEntity) -> Unit
    ) {
        _binding.habitItemTextView.text = item.habitName

        setBackgroundForImageButton(
            isDateExist(item.habitId!!, LocalDate.now()),
            _binding.habitItemBttn
        )
        _binding.habitItemBttn.setOnClickListener { view ->
            onClickImageButton(view, item, isDateExist, insertToday, removeToday)
        }

        val listOfImageView: List<ImageView> = listOf(
            _binding.habitItemYesterday,
            _binding.habitItemTwodayago,
            _binding.habitItemThreedayago
        )
        for (imageView in listOfImageView){
            val index = listOfImageView.indexOf(imageView)
            val dateEntity = item.getDateEntityByDate(getDaysAgo((index+1).toLong()))
            if(dateEntity != null){
                setBackgroundForImageView(imageView, true)
            }else{
                setBackgroundForImageView(imageView, false)
            }

        }
    }

    private fun onClickImageButton(
        view: View,
        item: HabitWDate,
        isDateExist: (id: Long, date: LocalDate) -> Boolean,
        insertToday: (HabitEntity) -> Unit,
        deleteToday: (DateEntity) -> Unit
    ) {
        view.setTag("" + item.habitEntity.id_habit)
        if (!isDateExist(item.habitId!!, LocalDate.now())) {
            insertToday(item.habitEntity)
        } else {
            val date = item.datesEntities.first {
                DateConverter.toString(it.date) == DateConverter.toString(LocalDate.now())
            }
            deleteToday(date)
        }
    }

    private fun setBackgroundForImageButton(flag: Boolean, view: View) {
        val drawable_id =
            if (flag) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        (view as ImageButton).setBackgroundResource(drawable_id)

    }


    private fun setBackgroundForImageView(view: ImageView, flag: Boolean) {
        val drawable_id =
            if (flag) R.drawable.ic_baseline_done_24 else R.drawable.ic_baseline_close_24
        view.foreground = this.itemView.resources.getDrawable(drawable_id)
        //view.setBackgroundColor(this.itemView.resources.getColor(R.color.md_theme_light_background))
    }

    private fun getDaysAgo(daysAgo: Long): LocalDate {
        val today = LocalDate.now()
        return today.minusDays(daysAgo)
    }
}