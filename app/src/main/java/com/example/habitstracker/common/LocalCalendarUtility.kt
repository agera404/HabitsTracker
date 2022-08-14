package com.example.habitstracker.common

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.util.Log
import com.example.habitstracker.models.LocalCalendar
import java.util.*


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
        return calendars.firstOrNull { (it?.id ?: -1) == id }
    }

    fun removeEvent(eventID: Long?){
        val cr: ContentResolver = context.getContentResolver()
        val values = ContentValues()
        var deleteUri: Uri? = null
        deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID!!)
        val rows: Int = context.getContentResolver().delete(deleteUri, null, null)
    }

    fun createEvent(idCalendar: Int, beginTime: Calendar, title: String, ): ContentValues {
        Log.d("Calendar_debug","Trying to create a new event...")
        return ContentValues().apply{
            put(CalendarContract.Events.CALENDAR_ID, idCalendar)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, "added from Habits Tracker")
            put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis())
            put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis())
            put(CalendarContract.Events.ALL_DAY, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
    }

    fun addEventToCalendar(event: ContentValues): Long? {
        Log.d("Calendar_debug","Trying to add a new event...")
        val eventUri: Uri
        eventUri =
            Uri.parse("content://com.android.calendar/events")
        val uri: Uri? = context.contentResolver.insert(eventUri, event)
        val eventId = uri?.lastPathSegment?.toLongOrNull()
        Log.d("Calendar_debug", "Event id is $eventId")
        return eventId
    }

}