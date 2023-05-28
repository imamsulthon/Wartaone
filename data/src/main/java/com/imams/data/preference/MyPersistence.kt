package com.imams.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MyPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val myApiKey = stringPreferencesKey("my_api_key")
    private val forceLocal = booleanPreferencesKey("force_local")
    private val locale = stringPreferencesKey("news_locale")
    private val language = stringPreferencesKey("news_language")

    suspend fun saveApiKey(apiKey: String) {
        dataStore.edit { pref ->
            pref[myApiKey] = apiKey
        }
    }

    fun getApiKey(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[myApiKey] ?: ""
        }
    }

    suspend fun forceLocal(force: Boolean) {
        dataStore.edit {
            it[forceLocal] = force
        }
    }

    fun isForceLocal(): Flow<Boolean> = dataStore.data.map {
            pref -> pref[forceLocal] ?: false
    }

    suspend fun setLocale(apiKey: String) {
        dataStore.edit { pref ->
            pref[locale] = apiKey
        }
    }

    fun getLocale(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[locale] ?: ""
        }
    }

    suspend fun setLanguage(apiKey: String) {
        dataStore.edit { pref ->
            pref[language] = apiKey
        }
    }

    fun getLanguage(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[language] ?: ""
        }
    }
}