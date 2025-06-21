package com.example.financialapp.domain.usecases

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.repositories.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Category>> {
        return repository.getCategories()
    }
}