package com.example.core_domain.models

data class Income(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val icon: String,
    val amount: String,
    val account: String,
    val comment: String? = null,
    val date: String,
    val currency: String,
    val updatedAt: String
    ) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}