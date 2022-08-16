package com.example.habitstracker.ui

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.habitstracker.R
import com.example.habitstracker.models.DateConverter
import java.time.LocalDate

class HistoryMap(context: Context, private val _width: Int, val _height: Int, private val dateSet: List<LocalDate>,
                 private val isLarge:Boolean, val passDate:(date: LocalDate)->Unit): View(context), View.OnTouchListener {

    private var today: LocalDate
        get() {
            return LocalDate.now()
        }
        set(value) {}

    private var arrayOfSad: ArrayList<SquareAndDate>
    get() {
        return createSadObjects()
    }
        set(value) {}

    init {
        setOnTouchListener(this)
        if(isLarge){
            START_X_CANVAS = 53 * ((RECT_SIZE* 3)+ SPACE_BETWEEN_Y)
        }else{
            START_X_CANVAS = _width - 40f
        }
    }
    fun getXstart(): Float{
        return START_X_CANVAS+10F
    }
    companion object {
        var START_X_CANVAS = 0f
        const val RECT_SIZE = 35f
        const val START_Y_CANVAS = 100f
        const val SPACE_BETWEEN_Y = 7f
        const val ROUND_REC_RX_RY = 2f
    }
    private fun createSadObjects():ArrayList<SquareAndDate>{
        //храним все квадратики с числами тут
        val array: ArrayList<SquareAndDate> = ArrayList()

        var day = today
        var temp_day = today
        var startX = START_X_CANVAS
        var month = 0

        while (month<13){
            while (day.isAfter(temp_day.withDayOfMonth(1).minusDays(1))){
                val text = day.dayOfMonth.toString()
                val _dayOfWeek = day.dayOfWeek.value
                val rec_size = if (isLarge) RECT_SIZE * 3 else RECT_SIZE
                //если неделя кончилась, передвигаем координату Х левее
                if (_dayOfWeek == 7){
                    startX = startX - rec_size - SPACE_BETWEEN_Y
                }
                //проверяем не вышла ли координата Х за пределы экрана
                if (startX <=25){
                    return array
                }

                //создаем квадрат со скругленными углами
                val rectF = RectF().apply {
                    top = START_Y_CANVAS + ((rec_size + SPACE_BETWEEN_Y)*(_dayOfWeek-1))
                    bottom = top + rec_size
                    left = startX
                    right = left - rec_size
                }
                //проверяем есть ли эта дата в бд
                val date = dateSet.find { DateConverter.toString(it).equals(DateConverter.toString(day)) }

                val paint: Paint = Paint().apply {
                    isAntiAlias = true
                    if (date!=null){
                        color = context.resources.getColor(R.color.colorAccent)
                        style = Paint.Style.FILL_AND_STROKE
                    }else{
                        color = Color.GRAY
                        style = Paint.Style.STROKE
                    }

                }
                //добавляем все данные в массив
                array.add(SquareAndDate(rectF, text, paint, day, isLarge))

                day = day.minusDays(1)
            }
            temp_day = day
            month++
            Log.d("dLog","next month: " + month)
        }

        return array
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null){
            for (item in arrayOfSad){
                canvas.drawRoundRect(item.rectF, ROUND_REC_RX_RY, ROUND_REC_RX_RY, item.paint)
                item.drawText(canvas)
                if (item.date.dayOfMonth < 7){
                    if (item.date.dayOfWeek.value == 1){
                        canvas.drawText(item.date.month.toString(),item.rectF.left,START_Y_CANVAS - 15, item.textPaint)
                    }
                }
            }
        }

    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if (event != null && isLarge) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                val sad = getSadObject(event)
                if (sad != null){
                    Log.d("dLog",sad.date.toString())
                    passDate(sad.date)
                }
            }
        }
        return true
    }

    private fun getSadObject(event: MotionEvent): SquareAndDate? {
        val x = event.x
        val y = event.y
        for (item in arrayOfSad){
            if (item.rectF.right<x && x<item.rectF.left && item.rectF.top<y && y<item.rectF.bottom){
                return item
            }
        }
        return null
    }
}

class SquareAndDate(val rectF: RectF, val text: String, val paint: Paint, val date: LocalDate, isBig: Boolean){
    val textPaint: Paint = Paint().apply {
        if (isBig){
            textSize = 44f
        }else{
            textSize = 22f
        }
        color = Color.BLACK
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
    }

    fun drawText(canvas: Canvas){
        val textWidth = textPaint.measureText(text, 0, text.length)
        val textSize = textPaint.textSize
        canvas.drawText(text, rectF.centerX() - (textWidth/2),
            rectF.centerY() + (textSize / 2)-4, textPaint)
    }
}