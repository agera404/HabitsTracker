package com.example.habitstracker.data.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.habitstracker.data.NotificationReceiver
import com.example.habitstracker.models.NotificationData
import java.time.ZoneId

interface INotificator {
    fun createNotification()
}

class Notificator(val context: Context, val info: NotificationData? = null) : INotificator {

    override fun createNotification() {
        val notifyIntent = Intent(context, NotificationReceiver::class.java)
        val bundle = Bundle()
        bundle.putParcelable("Notification_Info", info)
        notifyIntent.putExtra(
            "Bundle", bundle
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            info!!.id,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val timeInMillis = info.date.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        if (timeInMillis != null) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun removeNotification(id: Long) {
        val notifyIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.toInt(),
            notifyIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}