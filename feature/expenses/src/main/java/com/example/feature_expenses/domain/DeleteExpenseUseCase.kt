package com.example.feature_expenses.domain

import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(expenseId: Int): NetworkResult<Unit> {
        return repository.deleteTransactionById(expenseId)
    }
}