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
import com.example.sclink.data.repository.DataStoreOperationsImpl.PrefsKey.onRemindKey
import com.example.sclink.data.repository.DataStoreOperationsImpl.PrefsKey.weekTypeKey
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

    private inline fun <reified T : Any> getValueFromDataStore(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    override fun getTypeOfWeek(): Flow<String> {
        val defaultWeekType = context.getString(R.string.upper_week)

        return getValueFromDataStore(weekTypeKey, defaultWeekType)
    }

    override suspend fun setTypeOfWeek(typeOfWeek: String) {
        dataStore.edit { preferences ->
            preferences[weekTypeKey] = typeOfWeek
        }
    }

    override fun getNotificationBtnState(): Flow<Boolean> {
        val defaultNotificationState = false
        return getValueFromDataStore(onRemindKey, defaultNotificationState)
    }

    override suspend fun setNotificationBtnState(isClicked: Boolean) {
        dataStore.edit { preferences ->
            preferences[onRemindKey] = isClicked
        }
    }
}
