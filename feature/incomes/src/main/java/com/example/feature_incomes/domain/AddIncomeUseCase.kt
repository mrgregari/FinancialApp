package com.example.feature_incomes.domain

import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class AddIncomeUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(income: Income, accountId: Int, categoryId: Int): NetworkResult<Unit> {
        return transactionRepository.addIncome(income, accountId, categoryId)
    }
}