package com.example.sclink.alarm_manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.sclink.utils.Constants.ALARM_INTENT_STRING_EXTRA
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

class AndroidAlarmScheduler(
    private val context: Context,
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(typeOfWeek: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.putExtra(ALARM_INTENT_STRING_EXTRA, typeOfWeek)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 48)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel() {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}