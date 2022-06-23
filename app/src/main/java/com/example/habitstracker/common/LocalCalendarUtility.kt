package com.example.habitstracker.common

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.habitstracker.models.LocalCalendar

class LocalCalendarUtility(val context: Context) {


     fun getAllAvailableCalendars(): Array<LocalCalendar?> {
        val cursor: Cursor? = context.contentResolver.query(
            Uri.parse("content://com.android.calendar/calendars"),
            arrayOf("_id", "calendar_displayName"),
            null,
            null,
            null
        )
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val localCalendars = arrayOfNulls<LocalCalendar>(cursor.count)
                for (i in 0 until cursor.count) {
                    localCalendars[i] =
                        LocalCalendar(id = cursor.getInt(0), name = cursor.getString(1))
                    cursor.moveToNext()
                }
                return localCalendars
            }
        }
        return arrayOf()
    }
    fun getLocalCalendarById(id: Int): LocalCalendar? {
        val calendars = getAllAvailableCalendars()
        return calendars.firstOrNull { it?.id ?: -1 == id}
    }
}