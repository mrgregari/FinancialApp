package com.example.core_domain.usecases

import com.example.core_domain.repositories.CategoryRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): NetworkResult<Unit> {
        return try {
            val result = categoryRepository.getCategories()
            if (result is NetworkResult.Success) {
                NetworkResult.Success(Unit)
            } else if (result is NetworkResult.Error) {
                result
            } else {
                NetworkResult.Error(Exception("Не удалось синхронизировать категории"))
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
} 