package com.example.feature_expenses.domain

import com.example.core_domain.models.Expense
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    val repository: TransactionRepository
) {
    suspend operator fun invoke(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return repository.updateExpense(expense, accountId, categoryId)
    }
}