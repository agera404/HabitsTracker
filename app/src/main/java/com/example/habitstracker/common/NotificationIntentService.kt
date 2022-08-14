package com.example.habitstracker.common


import android.app.*
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.habitstracker.MainActivity
import com.example.habitstracker.models.NotificationData
import java.time.LocalTime


class NotificationIntentService : IntentService("MyNewIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        val context = applicationContext
        val info = intent?.getBundleExtra("Bundle")?.getParcelable<NotificationData>("Notification_Info")
        if (info != null){
            val builder: Notification.Builder = Notification.Builder(this).apply {
                setSmallIcon(info.drawable_id)
                setContentTitle(info.title)
                setContentText(info.body)
                val notifyIntent = Intent(context, MainActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                setContentIntent(pendingIntent)
            }
            val channelId = "Habits notification"
            val channel = NotificationChannel(channelId,channelId, NotificationManager.IMPORTANCE_HIGH)


            val managerCompat = NotificationManagerCompat.from(this)
            managerCompat.createNotificationChannel(channel)
            builder.setChannelId(channelId)
            val notificationCompat: Notification = builder.build()
            managerCompat.notify(info.id.toInt(), notificationCompat)

        }
    }

}