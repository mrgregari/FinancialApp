package com.example.financialapp.data.datasources.remote

import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.models.TransactionDto
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