package com.example.financialapp.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.domain.models.Category
import com.example.financialapp.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.financialapp.ui.screens.categories.CategoriesUiState

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val uiState: StateFlow<CategoriesUiState> = _uiState

    fun loadCategories() {
        _uiState.value = CategoriesUiState.Loading
        viewModelScope.launch {
            val result = getCategoriesUseCase()
            _uiState.value = result.fold(
                onSuccess = { CategoriesUiState.Success(it) },
                onFailure = { CategoriesUiState.Error(it) }
            )
        }
    }
}