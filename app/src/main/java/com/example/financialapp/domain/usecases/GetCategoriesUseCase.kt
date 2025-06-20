package com.example.financialapp.domain.usecases

import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.repositories.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getAllCategories()
    }
}