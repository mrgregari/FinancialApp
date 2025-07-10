package com.example.core_domain.repositories

import com.example.core_domain.models.Category
import com.example.core_network.network.NetworkResult

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