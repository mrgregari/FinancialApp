package com.example.feature_incomes.domain

import com.example.core_data.domain.models.Income
import com.example.core_data.domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
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