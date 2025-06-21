package com.example.financialapp.ui.screens.categories

import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.usecases.GetCategoriesUseCase
import com.example.financialapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel() {

    init {
        this.networkState = networkState
        this.errorHandler = errorHandler
        initializeNetworkState()
    }

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        loadCategories()
    }

    fun retry() {
        loadCategories()
    }

    private fun loadCategories() {
        safeApiCall(
            apiCall = { getCategoriesUseCase() },
            onSuccess = { categories ->
                _categories.value = categories
            }
            )
    }
}