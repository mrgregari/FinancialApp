package com.example.core_data.utils

fun getCurrencySymbol(currency: String): String = when (currency) {
    "RUB" -> "₽"
    "USD" -> "$"
    "EUR" -> "€"
    // Добавь другие валюты по необходимости
    else -> currency
} 