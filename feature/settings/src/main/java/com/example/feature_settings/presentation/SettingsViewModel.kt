package com.example.feature_settings.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.repositories.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val syncPrefs: SharedPreferences,
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val _lastSyncText = MutableStateFlow("")
    val lastSyncText: StateFlow<String> = _lastSyncText.asStateFlow()

    init {
        viewModelScope.launch {
            val lastSyncMillis = syncPrefs.getLong("last_sync_time", 0L)
            _lastSyncText.value = if (lastSyncMillis > 0) {
                val date = Date(lastSyncMillis)
                SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)
            } else {
                "Синхронизация ещё не проводилась"
            }
        }
    }

    val isDarkTheme: StateFlow<Boolean> = themeRepository.isDarkTheme
    fun setDarkTheme(enabled: Boolean) {
        themeRepository.setDarkTheme(enabled)
    }
} 