package com.example.core_data.remote.remoteDataSource

import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.api.FinancialApi
import jakarta.inject.Inject

/**
 * Transaction date source.
 * Gets data from API
 */
class TransactionRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getTransactions(accountId: Int, startDate: String?, endDate: String?): List<TransactionDto> =
        api.getTransactions(accountId, startDate, endDate)
}