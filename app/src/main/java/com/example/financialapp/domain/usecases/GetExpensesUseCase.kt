package com.example.financialapp.domain.usecases

import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.repositories.TransactionRepository
import javax.inject.Inject
import java.util.*

class GetExpensesUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ): Result<List<Expense>> {
        return repository.getExpenses(accountId, startDate, endDate)
    }
}