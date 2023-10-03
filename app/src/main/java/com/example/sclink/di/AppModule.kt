package com.example.sclink.di

import android.content.Context
import androidx.room.Room
import com.example.sclink.alarm_manager.AlarmScheduler
import com.example.sclink.alarm_manager.AndroidAlarmScheduler
import com.example.sclink.data.local.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFoldersDao(database: Database) = database.foldersDao

    @Provides
    @Singleton
    fun provideLessonsDao(database: Database) = database.lessonsDao

    @Provides
    @Singleton
    fun provideAndroidAlarmScheduler(
        @ApplicationContext
        context: Context
    ): AlarmScheduler {
        return AndroidAlarmScheduler(context = context)
    }

}