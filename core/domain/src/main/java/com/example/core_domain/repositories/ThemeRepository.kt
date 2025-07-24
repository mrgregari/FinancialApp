package com.example.core_domain.repositories

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {
    val isDarkTheme: StateFlow<Boolean>
    fun setDarkTheme(enabled: Boolean)
} 