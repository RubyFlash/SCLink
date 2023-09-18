package com.example.sclink.di

import android.content.Context
import androidx.room.Room
import com.example.sclink.data.local.Database
import com.example.sclink.data.repository.WeekTypeRepositoryImpl
import com.example.sclink.domain.repository.WeekTypeRepository
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
    fun provideWeekTypeRepository(@ApplicationContext context: Context) : WeekTypeRepository {
        return WeekTypeRepositoryImpl(context = context)
    }

}