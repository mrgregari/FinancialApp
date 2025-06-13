package com.example.financialapp.domain.model

data class Category(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val icon: String,
    val isIncome: Boolean
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
