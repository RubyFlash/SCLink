package com.example.sclink.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {
    fun getTypeOfWeek(): Flow<String>
    suspend fun setTypeOfWeek(typeOfWeek: String)

    fun getNotificationBtnState(): Flow<Boolean>
    suspend fun setNotificationBtnState(onClicked: Boolean)
}