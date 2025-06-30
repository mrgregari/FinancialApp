package com.example.financialapp.data.mappers

import com.example.financialapp.data.dto.TransactionDto
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import com.example.financialapp.ui.utils.getCurrencySymbol
import javax.inject.Inject

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