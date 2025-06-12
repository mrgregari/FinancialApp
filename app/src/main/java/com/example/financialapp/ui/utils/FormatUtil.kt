package com.example.financialapp.ui.utils

fun formatNumber(number: Int): String {
    return "%,d".format(number).replace(",", " ")
}