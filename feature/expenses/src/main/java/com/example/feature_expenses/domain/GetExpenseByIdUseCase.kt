package com.example.feature_expenses.domain

import com.example.core_domain.models.Expense
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(
        id: Int,
    ): NetworkResult<Expense> {
        return repository.getExpenseById(id)
    }
}