package com.example.financialapp.data.remote.remoteDataSource

import com.example.financialapp.data.remote.api.FinancialApi
import com.example.financialapp.data.remote.dto.TransactionDto
import javax.inject.Inject

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