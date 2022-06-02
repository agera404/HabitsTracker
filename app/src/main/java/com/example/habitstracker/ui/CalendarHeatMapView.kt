package com.example.habitstracker.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import java.time.LocalDate
import java.util.*

class CalendarHeatMapView(context: Context, val minDate: LocalDate, val dateSet: List<LocalDate>) :
    GridLayout(context) {
    var prams: GridLayout.LayoutParams = LayoutParams()
    var dates = listOf<LocalDate>()
        get() {
            var startDate = getStartDate()
            var endDate = LocalDate.now()
            var list = mutableListOf<LocalDate>()
            while (endDate.isAfter(startDate)) {
                list.add(startDate)
                startDate = startDate.plusDays(1)
            }
            list.add(endDate)
            return list
        }

    init {
        setPrams()
        createHeatMap()
    }

    fun createHeatMap() {
        for (day in dates) {
            var flag = false
            for (date in dateSet) {
                if (DateConverter.toString(day).equals(DateConverter.toString(date))) {
                    flag = true
                    break
                } else {
                    flag = false
                }
            }
            if (flag) {
                this.addView(createImageView(true, DateConverter.toString(day)!!))
            } else {
                this.addView(createImageView(false, DateConverter.toString(day)!!))
            }
        }
        addDayOfWeek()
    }

    private fun addDayOfWeek() {
        var dayOfWeek = listOf<Pair<Int, String>>(
            Pair(0, "M"), Pair(1, "T"),
            Pair(2, "W"), Pair(3, "T"),
            Pair(4, "F"), Pair(5, "S"),
            Pair(6, "S")
        )
        for (day in dayOfWeek) {
            this.addView(TextView(context).apply {
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                isAllCaps = true
                textSize = 20F
                setText(day.second)
            }, day.first)
        }
    }


    private fun getStartDate(): LocalDate {

        val today = LocalDate.now()
        var startDate = today.minusMonths(3)
        startDate = startDate.plusDays(7)
        val _dayOfWeek: Long = (startDate.dayOfWeek.value).toLong()
        startDate = startDate.minusDays(_dayOfWeek-1)
        return startDate
    }

    private fun setPrams() {
        prams.width = LayoutParams.WRAP_CONTENT
        prams.height = 300
        this.prams = prams
        this.rowCount = 7
        //this.columnCount = getTotalWeeksInYear(2022)
        this.orientation = GridLayout.VERTICAL
    }


    private fun getTotalWeeksInYear(year: Int): Int {
        val mCalendar: Calendar = GregorianCalendar()
        mCalendar[Calendar.YEAR] = year // Set only year
        mCalendar[Calendar.MONTH] = Calendar.DECEMBER // Don't change
        mCalendar[Calendar.DAY_OF_MONTH] = 31 // Don't change
        return mCalendar[Calendar.WEEK_OF_YEAR]
    }

    private fun createImageView(flag: Boolean, date: String): View {
        val backgroundParams = object {
            var drawable: Int? = null
            var color: Int? = null
        }
        if (flag) {
            backgroundParams.apply {
                drawable = R.drawable.ic_baseline_done_24
                color = Color.GREEN
            }
        } else {
            backgroundParams.apply {
                drawable = R.drawable.ic_baseline_close_24
                color = Color.GRAY
            }
        }
        var imageView: ImageView = ImageView(context)
        imageView.tag = date
        imageView.minimumWidth = 75
        imageView.minimumHeight = 75
        imageView.foreground = context.resources.getDrawable(backgroundParams.drawable!!)
        imageView.setBackgroundColor(backgroundParams.color!!)
        return imageView
    }
}