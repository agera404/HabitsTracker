package com.example.habitstracker.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.example.habitstracker.models.NotificationData

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val info = intent?.getBundleExtra("Bundle")?.parcelable<NotificationData>("Notification_Info")

        val nintent = Intent(context, NotificationIntentService::class.java)
        val bundle = Bundle()
        bundle.putParcelable("Notification_Info", info)
        nintent.putExtra(
            "Bundle", bundle
        )
        context.startService(nintent)
    }
}