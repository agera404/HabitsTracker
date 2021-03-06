package com.example.habitstracker.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.habitstracker.models.NotificationData


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("dLog", "intent == null is " + (intent == null).toString())
        val info = intent?.getBundleExtra("Bundle")?.getParcelable<NotificationData>("Notification_Info")

        Log.d("dLog", "info " + (info?.title ?: "is null"))
        val nintent = Intent(context, MyNewIntentService::class.java)
        val bundle = Bundle()
        bundle.putParcelable("Notification_Info", info)
        nintent.putExtra(
            "Bundle", bundle
        )
        context.startService(nintent)
        Log.d("dLog","onReceive()")
    }
}