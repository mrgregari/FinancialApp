package com.example.financialapp.data.api

import com.example.financialapp.data.models.AccountDTO
import com.example.financialapp.data.models.CategoryDTO
import retrofit2.http.GET

interface FinancialApi {


    @GET("categories")
    suspend fun getCategories(): List<CategoryDTO>

    @GET("accounts")
    suspend fun getAccounts(): List<AccountDTO>

}