package com.example.core_data.usecases

import com.example.core_data.domain.models.Category
import com.example.core_data.domain.repositories.CategoryRepository
import com.example.core_data.network.NetworkResult
import jakarta.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Category>> {
        return repository.getCategories()
    }
}