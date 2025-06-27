package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Category

/**
 * Repository for managing user categories data.
 * Provides access to categories information
 */

interface CategoryRepository {
    /**
     * Retrieves all user categories.
     *
     * @return List of categories wrapped in NetworkResult
     */

    suspend fun getCategories(): NetworkResult<List<Category>>
}