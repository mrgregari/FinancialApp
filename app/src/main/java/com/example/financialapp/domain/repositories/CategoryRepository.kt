package com.example.financialapp.domain.repositories

import com.example.financialapp.domain.models.Category

interface CategoryRepository {

    suspend fun getAllCategories() : Result<List<Category>>
}