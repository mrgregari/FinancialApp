package com.example.financialapp.domain.model

data class Account(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val balance: Int,
    val currency: String
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
