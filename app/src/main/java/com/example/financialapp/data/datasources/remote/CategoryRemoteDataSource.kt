package com.example.financialapp.data.datasources.remote

import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.models.CategoryDto
import javax.inject.Inject

/**
 * Category date source.
 * Gets data from API
 */
class CategoryRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getCategories(): List<CategoryDto> = api.getCategories()
}