package com.example.sclink.domain.repository

import kotlinx.coroutines.flow.Flow

interface WeekTypeRepository {
    fun getTypeOfWeek(): Flow<String>
    suspend fun setTypeOfWeek(typeOfWeek: String)
}