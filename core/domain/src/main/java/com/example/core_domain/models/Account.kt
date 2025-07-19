package com.example.core_domain.models

data class Account(
    val id: Int = UNDEFINED_ID,
    val name: String,
    val balance: String,
    val currency: String,
    val updatedAt: String
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}