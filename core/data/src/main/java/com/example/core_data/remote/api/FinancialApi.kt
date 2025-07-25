package com.example.core_data.remote.api

import com.example.core_data.remote.dto.AccountDto
import com.example.core_data.remote.dto.CategoryDto
import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.dto.UpdateAccountDto
import com.example.core_data.remote.dto.CreateTransactionDto
import com.example.core_data.remote.dto.UpdateTransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API interface for financial data.
 * Defines network endpoints for accounts, categories and transactions.
 */
interface FinancialApi {

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @POST("transactions")
    suspend fun postTransaction(@Body transaction: CreateTransactionDto): Response<TransactionDto>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int
    ) : Response<Unit>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body update: UpdateTransactionDto
    ): Response<Unit>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean,
    ): List<CategoryDto>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") transactionId: Int
    ): TransactionDto

    @GET("accounts")
    suspend fun getAccounts(): List<AccountDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionDto>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body update: UpdateAccountDto
    ): Response<Unit>


}