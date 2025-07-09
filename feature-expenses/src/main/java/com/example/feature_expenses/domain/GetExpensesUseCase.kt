package com.example.financialapp.domain.usecases

import com.example.core_data.domain.models.Expense
import com.example.core_data.domain.repositories.TransactionRepository
import com.example.core_data.network.NetworkResult
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