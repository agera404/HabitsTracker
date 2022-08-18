package com.example.habitstracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.LocalCalendarItemBinding
import com.example.habitstracker.models.LocalCalendar

class LocalCalendarsAdapter(
    private val localCalendars: MutableList<LocalCalendar?>,
    private val setLocalCalendarId: (Int) -> Unit,
    private val closeDialog: () -> Unit
) :
    RecyclerView.Adapter<LocalCalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalCalendarViewHolder {
        val _binding =
            LocalCalendarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocalCalendarViewHolder(_binding)
    }


    override fun onBindViewHolder(holder: LocalCalendarViewHolder, position: Int) {
        val localCalendar = localCalendars[position]
        if (localCalendar != null) {
            holder.bind(localCalendar)
            holder.itemView.setOnClickListener {
                setLocalCalendarId(localCalendar.id)
                closeDialog()
            }
        }
    }

    override fun getItemCount(): Int {
        return localCalendars.size
    }
}