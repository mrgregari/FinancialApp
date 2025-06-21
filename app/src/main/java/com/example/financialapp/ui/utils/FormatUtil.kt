package com.example.financialapp.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatNumber(number: String): String {
    val parts = number.split(".")
    val integerPart = parts[0].replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1 ")
    return if (parts.size > 1) {
        val decimalPart = parts[1].padEnd(2, '0').take(2)
        if (decimalPart == "00") integerPart else "$integerPart.$decimalPart"
    } else {
        integerPart
    }
}

fun getCurrencySymbol(currency: String): String = when (currency) {
    "RUB" -> "₽"
    "EUR" -> "€"
    "USD" -> "$"
    else -> "₽"
}

fun formatAmountWithCurrency(amount: Double, currency: String): String {
    return "${formatNumber(amount.toString())} $currency"
}

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