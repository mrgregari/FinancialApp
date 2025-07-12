package com.example.feature_incomes.domain

import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class UpdateIncomeUseCase @Inject constructor(
    val repository: TransactionRepository
) {
    suspend operator fun invoke(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return repository.updateIncome(income, accountId, categoryId)
    }
} 