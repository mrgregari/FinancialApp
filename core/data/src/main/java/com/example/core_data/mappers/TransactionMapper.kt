package com.example.core_data.mappers

import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.dto.CreateTransactionDto
import com.example.core_data.remote.dto.UpdateTransactionDto
import com.example.core_data.utils.getCurrencySymbol
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income


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


    fun fromExpenseToCreateDto(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): CreateTransactionDto {
        return CreateTransactionDto(
            accountId = accountId,
            categoryId = categoryId,
            amount = expense.amount,
            transactionDate = expense.date,
            comment = expense.comment
        )
    }

    fun fromExpenseToUpdateDto(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): UpdateTransactionDto {
        return UpdateTransactionDto(
            id = expense.id,
            accountId = accountId,
            categoryId = categoryId,
            amount = expense.amount,
            transactionDate = expense.date,
            comment = expense.comment
        )
    }

    fun fromIncomeToCreateDto(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): CreateTransactionDto {
        return CreateTransactionDto(
            accountId = accountId,
            categoryId = categoryId,
            amount = income.amount,
            transactionDate = income.date,
            comment = income.comment
        )
    }

    fun fromIncomeToUpdateDto(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): UpdateTransactionDto {
        return UpdateTransactionDto(
            id = income.id,
            accountId = accountId,
            categoryId = categoryId,
            amount = income.amount,
            transactionDate = income.date,
            comment = income.comment
        )
    }
}