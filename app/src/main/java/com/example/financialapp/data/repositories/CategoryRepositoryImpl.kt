package com.example.financialapp.data.repositories

import com.example.financialapp.data.datasources.remote.CategoryRemoteDataSource
import com.example.financialapp.data.mappers.CategoryMapper
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.repositories.CategoryRepository
import com.example.financialapp.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteDataSource: CategoryRemoteDataSource,
    private val mapper: CategoryMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CategoryRepository {

    override suspend fun getCategories(): NetworkResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val dtos = categoryRemoteDataSource.getCategories()
                val categories = dtos.map { mapper.fromDtoToCategory(it) }
                NetworkResult.Success(categories)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }
}
