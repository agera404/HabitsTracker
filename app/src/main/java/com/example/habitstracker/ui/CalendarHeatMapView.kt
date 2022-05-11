package com.example.habitstracker.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import org.w3c.dom.Text
import java.util.*

class CalendarHeatMapView(context: Context, val minDate: Date, val dateSet: List<Date>) :
    GridLayout(context) {
    var prams: GridLayout.LayoutParams = LayoutParams()
    var dates = listOf<Date>()
        get() {
            var startDate = getStartDate()
            var endDate = Date()
            var list = mutableListOf<Date>()
            while (endDate >= startDate) {
                list.add(startDate)
                startDate = Calendar.getInstance().apply { time = startDate }
                    .also { it.add(Calendar.HOUR_OF_DAY, 24) }.time
            }
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
                if (DateConverter.dateToString(day).equals(DateConverter.dateToString(date))) {
                    flag = true
                    break
                } else {
                    flag = false
                }
            }
            if (flag) {
                this.addView(createImageView(true, DateConverter.dateToString(day)!!))
            } else {
                this.addView(createImageView(false, DateConverter.dateToString(day)!!))
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


    private fun getStartDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        //calendar.time = minDate
        calendar.add(Calendar.MONTH, -3)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeek - 2))
        return calendar.time
    }

    private fun setPrams() {
        prams.width = LayoutParams.WRAP_CONTENT
        prams.height = 300
        this.prams = prams
        this.rowCount = 7
        this.columnCount = getTotalWeeksInYear(2022)
        this.orientation = GridLayout.VERTICAL
    }


    private fun getTotalWeeksInYear(year: Int): Int {
        val mCalendar: Calendar = GregorianCalendar()
        mCalendar[Calendar.YEAR] = year // Set only year
        mCalendar[Calendar.MONTH] = Calendar.DECEMBER // Don't change
        mCalendar[Calendar.DAY_OF_MONTH] = 31 // Don't change
        return mCalendar[Calendar.WEEK_OF_YEAR]
    }

    fun createImageView(flag: Boolean, date: String): View {
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