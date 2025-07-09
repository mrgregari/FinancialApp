package com.example.core_data.data.remoteDataSource

import com.example.core_data.data.dto.CategoryDto
import com.example.core_data.remote.api.FinancialApi
import jakarta.inject.Inject

/**
 * Category date source.
 * Gets data from API
 */
class CategoryRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getCategories(): List<CategoryDto> = api.getCategories()
}