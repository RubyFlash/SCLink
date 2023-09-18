package com.example.sclink.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.sclink.domain.repository.WeekTypeRepository
import com.example.sclink.utils.Constants.WEEK_TYPE_PREFS_KEY
import com.example.sclink.utils.Constants.WEEK_TYPE_PREFS_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = WEEK_TYPE_PREFS_NAME)

class WeekTypeRepositoryImpl @Inject constructor(
    private val context: Context
): WeekTypeRepository {

    private object PrefsKey {
        val weekTypeKey = stringPreferencesKey(name = WEEK_TYPE_PREFS_KEY)
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
                val weekType = preferences[PrefsKey.weekTypeKey] ?: ""
                weekType
            }
    }

    override suspend fun setTypeOfWeek(typeOfWeek: String) {
        dataStore.edit { preferences ->
            preferences[PrefsKey.weekTypeKey] = typeOfWeek
        }
    }

}