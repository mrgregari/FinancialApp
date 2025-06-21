package com.example.financialapp.data.models

data class TransactionDto(
    val id: Int,
    val account: AccountDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)