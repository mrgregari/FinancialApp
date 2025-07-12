package com.example.feature_incomes.domain

import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class GetIncomeByIdUseCase @Inject constructor(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(
        id: Int,
    ): NetworkResult<Income> {
        return repository.getIncomeById(id)
    }
} 