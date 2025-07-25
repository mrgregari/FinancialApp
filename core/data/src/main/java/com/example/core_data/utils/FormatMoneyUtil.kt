package com.example.core_data.utils

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

