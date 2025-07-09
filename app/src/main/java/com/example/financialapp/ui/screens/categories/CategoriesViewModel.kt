package com.example.financialapp.ui.screens.categories

import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.usecases.GetCategoriesUseCase
import com.example.financialapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {

    init {
        loadCategories()
        initializeNetworkState()
    }

    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    private val _searchText = MutableStateFlow<String>("")

    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        updateSuccessState()
    }

    private fun updateSuccessState() {
        val text = _searchText.value
        val filtered = if (text.isBlank()) {
            _categories.value
        } else {
            _categories.value.filter {
                it.doesMatchSearchQuery(text)
            }
        }
        _uiState.value = CategoriesUiState.Success(filtered, text)
    }


    fun retry() {
        loadCategories()
    }

    fun loadCategories() {
        safeApiCall(
            apiCall = { getCategoriesUseCase() },
            onSuccess = { categories ->
                _categories.value = categories
                updateSuccessState()
            },
            onError = { errorResId ->
                _uiState.value = CategoriesUiState.Error(errorResId)
            }
        )
    }
}