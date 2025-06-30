package com.example.financialapp.data.remote.remoteDataSource

import com.example.financialapp.data.remote.api.FinancialApi
import com.example.financialapp.data.dto.CategoryDto
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