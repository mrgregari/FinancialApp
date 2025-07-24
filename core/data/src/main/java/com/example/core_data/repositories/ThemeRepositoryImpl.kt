package com.example.core_data.repositories

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.core_domain.repositories.ThemeRepository
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(private val prefs: SharedPreferences) :
    ThemeRepository {
    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }

    private val _isDarkTheme = MutableStateFlow(prefs.getBoolean(KEY_DARK_THEME, false))
    override val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    override fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
        _isDarkTheme.value = enabled
    }
} 