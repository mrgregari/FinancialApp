package com.example.core_data.repositories

import com.example.core_data.remote.mappers.CategoryMapper
import com.example.core_data.remote.remoteDataSource.CategoryRemoteDataSource
import com.example.core_data.di.IODispatcher
import com.example.core_domain.models.Category
import com.example.core_domain.repositories.CategoryRepository
import com.example.core_network.network.NetworkResult
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

    override suspend fun getIncomeCategories(): NetworkResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val dtos = categoryRemoteDataSource.getCategoriesByType(true)
                val categories = dtos.map { mapper.fromDtoToCategory(it) }
                NetworkResult.Success(categories)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun getExpenseCategories(): NetworkResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val dtos = categoryRemoteDataSource.getCategoriesByType(false)
                val categories = dtos.map { mapper.fromDtoToCategory(it) }
                NetworkResult.Success(categories)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }
}
