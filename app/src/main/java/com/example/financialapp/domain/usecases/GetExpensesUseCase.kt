package com.example.financialapp.domain.usecases

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.repositories.TransactionRepository
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