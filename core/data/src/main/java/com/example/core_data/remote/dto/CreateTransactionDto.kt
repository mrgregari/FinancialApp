package com.example.core_data.remote.dto

data class CreateTransactionDto(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?
) 