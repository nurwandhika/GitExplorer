package com.example.fundamentalgithub.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.example.fundamentalgithub.utils.SettingsPreferences.Companion.SETTINGS_PREFERENCES
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES)

class SettingsPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getDarkMode() = dataStore.data.map {
        it[PREFS_DARK_MODE] ?: false
    }.asLiveData()

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.edit {
            it[PREFS_DARK_MODE] = darkMode
        }
    }

    companion object {
        const val SETTINGS_PREFERENCES = "settings_preferences"
        val PREFS_DARK_MODE = booleanPreferencesKey("dark_mode")

        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) = INSTANCE ?: synchronized(this) {
            val instance = SettingsPreferences(dataStore)
            INSTANCE = instance
            instance
        }
    }
}