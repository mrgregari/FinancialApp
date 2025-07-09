package com.example.core_data.data.mappers

import com.example.core_data.data.dto.TransactionDto
import com.example.core_data.domain.models.Expense
import com.example.core_data.domain.models.Income
import com.example.core_data.utils.getCurrencySymbol


import jakarta.inject.Inject

class TransactionMapper @Inject constructor() {

    fun fromDtoToExpense(dto: TransactionDto) = Expense(
        id = dto.id,
        title = dto.category.name,
        icon = dto.category.emoji,
        amount = dto.amount,
        account = dto.account.name,
        comment = dto.comment,
        date = dto.transactionDate,
        currency = getCurrencySymbol(dto.account.currency)
    )

    fun fromDtoToIncome(dto: TransactionDto) = Income(
        id = dto.id,
        title = dto.category.name,
        icon = dto.category.emoji,
        amount = dto.amount,
        account = dto.account.name,
        comment = dto.comment,
        date = dto.transactionDate,
        currency = getCurrencySymbol(dto.account.currency)
    )
}