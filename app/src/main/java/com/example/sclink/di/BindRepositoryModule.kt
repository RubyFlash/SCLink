package com.example.sclink.di

import com.example.sclink.data.repository.FoldersRepositoryImpl
import com.example.sclink.data.repository.LessonsRepositoryImpl
import com.example.sclink.domain.repository.FoldersRepository
import com.example.sclink.domain.repository.LessonsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsLessonsRepository(
        lessonsRepositoryImpl: LessonsRepositoryImpl
    ): LessonsRepository

    @Binds
    @Singleton
    abstract fun bindsFoldersRepository(
        foldersRepositoryImpl: FoldersRepositoryImpl
    ): FoldersRepository

}