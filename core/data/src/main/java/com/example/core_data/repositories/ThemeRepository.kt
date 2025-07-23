package com.example.core_data.repositories

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeRepository(private val prefs: SharedPreferences) {
    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }

    private val _isDarkTheme = MutableStateFlow(isDarkTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun isDarkTheme(): Boolean = prefs.getBoolean(KEY_DARK_THEME, false)

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
        _isDarkTheme.value = enabled
    }
} 