package com.example.financialapp.domain.models


data class Income(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val amount: Int,
    val account: String,
    val comment: String? = null,
    val date: String,

    ) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
