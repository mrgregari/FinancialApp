package com.example.financialapp.data.api

import com.example.financialapp.data.models.CategoryDTO
import retrofit2.http.GET

interface FinancialApi {


    @GET("categories")
    suspend fun getCategories(): List<CategoryDTO>

}