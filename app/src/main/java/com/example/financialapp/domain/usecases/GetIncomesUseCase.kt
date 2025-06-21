package com.example.financialapp.domain.usecases

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Income
import com.example.financialapp.domain.repositories.TransactionRepository
import java.util.Date
import javax.inject.Inject

class GetIncomesUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ): NetworkResult<List<Income>> {
        return repository.getIncomes(accountId, startDate, endDate)
    }

}