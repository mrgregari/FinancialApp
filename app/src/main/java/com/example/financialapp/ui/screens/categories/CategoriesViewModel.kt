package com.example.financialapp.ui.screens.categories

import androidx.lifecycle.viewModelScope
import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.usecases.GetCategoriesUseCase
import com.example.financialapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _searchText = MutableStateFlow<String>("")
    val searchText = _searchText.asStateFlow()


    val filteredCategories =
        combine(_categories, searchText) { categories, text ->
            if (text.isBlank()) {
                categories
            } else {
                categories.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            categories.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
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