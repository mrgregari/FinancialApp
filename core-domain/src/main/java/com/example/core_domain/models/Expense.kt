package com.example.core_domain.models

data class Expense(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val icon: String,
    val amount: String,
    val account: String,
    val currency: String,
    val comment: String? = null,
    val date: String
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}