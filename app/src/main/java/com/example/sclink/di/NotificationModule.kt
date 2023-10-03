package com.example.sclink.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.sclink.R
import com.example.sclink.utils.Constants.WEEK_TYPE_REMINDER_NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, WEEK_TYPE_REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_on)
    }

    @Provides
    @Singleton
    fun provideUpComingNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        val notificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                WEEK_TYPE_REMINDER_NOTIFICATION_CHANNEL_ID,
                "Week type reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        return notificationManager
    }
}