package com.example.core_data.remote.remoteDataSource

import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.api.FinancialApi
import com.example.core_data.remote.dto.CreateTransactionDto
import com.example.core_data.remote.dto.UpdateTransactionDto
import jakarta.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

/**
 * Transaction date source.
 * Gets data from API
 */
class TransactionRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDto> =
        api.getTransactions(accountId, startDate, endDate)

    suspend fun postTransaction(transaction: CreateTransactionDto): Response<TransactionDto> =
        api.postTransaction(transaction)

    suspend fun updateTransaction(
        transactionId: Int,
        transaction: UpdateTransactionDto
    ) {
        val response = api.updateTransaction(transactionId, transaction)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun getTransactionById(
        transactionId: Int
    ) : TransactionDto = api.getTransactionById(transactionId)

    suspend fun deleteTransactionById(
        transactionId: Int
    ) {
        val response = api.deleteTransaction(transactionId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
}