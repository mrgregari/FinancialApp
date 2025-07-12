package com.example.feature_incomes.domain

import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class DeleteIncomeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(incomeId: Int): NetworkResult<Unit> {
        return repository.deleteTransactionById(incomeId)
    }
} 