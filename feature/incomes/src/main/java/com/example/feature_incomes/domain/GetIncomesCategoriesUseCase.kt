package com.example.feature_incomes.domain

import com.example.core_domain.models.Category
import com.example.core_domain.repositories.CategoryRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class GetIncomesCategoriesUseCase @Inject constructor(
    val repository: CategoryRepository
) {

    suspend operator fun invoke(): NetworkResult<List<Category>> {
        return repository.getIncomeCategories()
    }
}
