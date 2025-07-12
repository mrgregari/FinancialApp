package com.example.core_domain.usecases

import com.example.core_domain.models.Category
import com.example.core_domain.repositories.CategoryRepository
import jakarta.inject.Inject
import com.example.core_network.network.NetworkResult

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Category>> {
        return repository.getCategories()
    }
}