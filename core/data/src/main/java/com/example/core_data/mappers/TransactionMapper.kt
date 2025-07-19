package com.example.core_data.mappers

import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.dto.CreateTransactionDto
import com.example.core_data.remote.dto.UpdateTransactionDto
import com.example.core_data.utils.getCurrencySymbol
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_data.local.entity.TransactionEntity


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
        currency = getCurrencySymbol(dto.account.currency),
        updatedAt = dto.updatedAt
    )

    fun fromDtoToIncome(dto: TransactionDto) = Income(
        id = dto.id,
        title = dto.category.name,
        icon = dto.category.emoji,
        amount = dto.amount,
        account = dto.account.name,
        comment = dto.comment,
        date = dto.transactionDate,
        currency = getCurrencySymbol(dto.account.currency),
        updatedAt = dto.updatedAt
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

    fun fromEntityToCreateDto(
        entity: TransactionEntity
    ): CreateTransactionDto {
        return CreateTransactionDto(
            accountId = entity.accountId,
            categoryId = entity.categoryId,
            amount = entity.amount,
            transactionDate = entity.transactionDate,
            comment = entity.comment
        )
    }

    fun fromEntityToUpdateDto(
        entity: TransactionEntity
    ): UpdateTransactionDto {
        return UpdateTransactionDto(
            id = entity.id,
            accountId = entity.accountId,
            categoryId = entity.categoryId,
            amount = entity.amount,
            transactionDate = entity.transactionDate,
            comment = entity.comment
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

    fun fromExpenseToEntity(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): TransactionEntity {
        return TransactionEntity(
            id = expense.id,
            accountId = accountId,
            categoryId = categoryId,
            amount = expense.amount,
            transactionDate = expense.date,
            comment = expense.comment,
            updatedAt = expense.updatedAt
        )
    }

    fun fromIncomeToEntity(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): TransactionEntity {
        return TransactionEntity(
            id = income.id,
            accountId = accountId,
            categoryId = categoryId,
            amount = income.amount,
            transactionDate = income.date,
            comment = income.comment,
            updatedAt = income.updatedAt
        )
    }

    fun fromEntityToExpense(
        entity: TransactionEntity,
        accountName: String,
        currency: String,
        categoryName: String,
        icon: String
    ): Expense {
        return Expense(
            id = entity.id,
            title = categoryName,
            icon = icon,
            amount = entity.amount,
            account = accountName,
            currency = currency,
            comment = entity.comment,
            date = entity.transactionDate,
            updatedAt = entity.updatedAt
        )
    }

    fun fromEntityToIncome(
        entity: TransactionEntity,
        accountName: String,
        currency: String,
        categoryName: String,
        icon: String
    ): Income {
        return Income(
            id = entity.id,
            title = categoryName,
            icon = icon,
            amount = entity.amount,
            account = accountName,
            currency = currency,
            comment = entity.comment,
            date = entity.transactionDate,
            updatedAt = entity.updatedAt
        )
    }

    fun fromDtoToEntity(dto: TransactionDto): TransactionEntity {
        return TransactionEntity(
            id = dto.id,
            accountId = dto.account.id,
            categoryId = dto.category.id,
            amount = dto.amount,
            transactionDate = dto.transactionDate,
            comment = dto.comment,
            updatedAt = dto.updatedAt,
            isDeleted = false,
            isSynced = true // remote всегда считается синхронизированным
        )
    }
}