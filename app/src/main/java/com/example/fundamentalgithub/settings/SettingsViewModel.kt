package com.example.fundamentalgithub.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundamentalgithub.utils.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingsPreferences) : ViewModel() {
    fun getDarkMode() = preferences.getDarkMode()

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            preferences.setDarkMode(darkMode)
        }
    }
}