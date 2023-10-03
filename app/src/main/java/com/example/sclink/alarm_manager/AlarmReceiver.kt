package com.example.sclink.alarm_manager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.sclink.R
import com.example.sclink.presentation.screens.foldersActivity.FoldersScreenActivity
import com.example.sclink.utils.Constants.ALARM_INTENT_STRING_EXTRA
import com.example.sclink.utils.Constants.WEEK_TYPE_REMINDER_NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val typeOfWeek = intent?.getStringExtra(ALARM_INTENT_STRING_EXTRA)
            showNotification(context = context, weekType = typeOfWeek!!)
        }
    }

    private fun showNotification(context: Context, weekType: String) {
        val intent = Intent(context, FoldersScreenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = notificationBuilder
            .setChannelId(WEEK_TYPE_REMINDER_NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification_content_text, weekType))
            .setSmallIcon(R.drawable.ic_notification_on)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(0, notification)
    }
}