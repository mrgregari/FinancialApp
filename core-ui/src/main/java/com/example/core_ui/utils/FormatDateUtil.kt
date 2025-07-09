package com.example.core_ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date?): String {
    return if (date != null) {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        formatter.format(date)
    } else {
        "Не выбрано"
    }
}

fun formatDateTime(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return ""
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = parser.parse(dateString)
        val formatter = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        // В случае ошибки парсинга, можно вернуть исходную строку или пустую
        ""
    }
}