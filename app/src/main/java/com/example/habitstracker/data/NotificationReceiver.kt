package com.example.habitstracker.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.habitstracker.models.NotificationData


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val info = intent?.getBundleExtra("Bundle")?.getParcelable<NotificationData>("Notification_Info")

        Log.d("dLog", "info " + (info?.title ?: "is null"))
        val nintent = Intent(context, NotificationIntentService::class.java)
        val bundle = Bundle()
        bundle.putParcelable("Notification_Info", info)
        nintent.putExtra(
            "Bundle", bundle
        )
        context.startService(nintent)
    }
}