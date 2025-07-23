package com.example.core_domain.usecases

import com.example.core_domain.models.Expense
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import java.util.Date
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ): NetworkResult<List<Expense>> {
        return repository.getExpenses(accountId, startDate, endDate)
    }
}