package com.example.sclink.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.sclink.R
import com.example.sclink.domain.repository.DataStoreOperations
import com.example.sclink.utils.Constants.DATA_STORE_PREFS_NAME
import com.example.sclink.utils.Constants.ON_REMIND_PREFS_KEY
import com.example.sclink.utils.Constants.WEEK_TYPE_PREFS_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_PREFS_NAME)

class DataStoreOperationsImpl(
    private val context: Context
): DataStoreOperations {

    private object PrefsKey {
        val weekTypeKey = stringPreferencesKey(name = WEEK_TYPE_PREFS_KEY)
        val onRemindKey = booleanPreferencesKey(name = ON_REMIND_PREFS_KEY)
    }

    private val dataStore = context.dataStore

    override fun getTypeOfWeek(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val weekType = preferences[PrefsKey.weekTypeKey] ?: context.getString(R.string.upper_week)
                weekType
            }
    }

    override suspend fun setTypeOfWeek(typeOfWeek: String) {
        dataStore.edit { preferences ->
            preferences[PrefsKey.weekTypeKey] = typeOfWeek
        }
    }

    override fun getNotificationBtnState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                val notificationBtnState = preferences[PrefsKey.onRemindKey] ?: false
                notificationBtnState
            }}

    override suspend fun setNotificationBtnState(onClicked: Boolean) {
        TODO("Not yet implemented")
    }
}