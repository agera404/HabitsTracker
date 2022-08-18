package com.example.habitstracker.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.LocalCalendarItemBinding
import com.example.habitstracker.models.LocalCalendar

class LocalCalendarViewHolder(private val _binding: LocalCalendarItemBinding) :
    RecyclerView.ViewHolder(_binding.root) {
        fun bind(localCalendar: LocalCalendar){
            _binding.localCalendarName.text = localCalendar.name
        }
}