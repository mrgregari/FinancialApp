package com.example.feature_expenses.domain

import com.example.core_domain.models.Expense
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(expense: Expense, accountId: Int, categoryId: Int): NetworkResult<Unit> {
        return transactionRepository.addExpense(expense, accountId, categoryId)
    }
} 