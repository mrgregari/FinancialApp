package com.example.financialapp.data.api

import com.example.financialapp.data.models.AccountDto
import com.example.financialapp.data.models.CategoryDto
import com.example.financialapp.data.models.TransactionDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FinancialApi {

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("accounts")
    suspend fun getAccounts(): List<AccountDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionDto>

}