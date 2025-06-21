package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Category

interface CategoryRepository {
    suspend fun getCategories(): NetworkResult<List<Category>>
}