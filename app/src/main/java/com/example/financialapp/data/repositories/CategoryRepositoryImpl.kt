package com.example.financialapp.data.repositories

import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.mappers.CategoryMapper
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: FinancialApi,
    private val mapper: CategoryMapper
) : CategoryRepository {
    override suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val dtos = api.getCategories()
            val categories = dtos.map { mapper.fromDto(it) }
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}