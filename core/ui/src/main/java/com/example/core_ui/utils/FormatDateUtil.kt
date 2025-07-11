package com.example.core_ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
        val formatter = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        // В случае ошибки парсинга, можно вернуть исходную строку или пустую
        ""
    }
}

fun formatToIso8601(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(date)
}

fun formatToIso8601Local(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    // sdf.timeZone = TimeZone.getDefault() // по умолчанию локальное время
    return sdf.format(date)
}

fun parseIso8601Date(dateString: String?): Date? {
    if (dateString.isNullOrEmpty()) return null
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        parser.parse(dateString)
    } catch (e: Exception) {
        null
    }
}