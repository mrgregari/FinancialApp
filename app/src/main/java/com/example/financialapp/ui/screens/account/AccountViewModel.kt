package com.example.financialapp.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.domain.usecases.GetAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
    val uiState: StateFlow<AccountUiState> = _uiState

    fun loadAccount() {
        _uiState.value = AccountUiState.Loading
        viewModelScope.launch {
            val result = getAccountUseCase()
            _uiState.value = result.fold(
                onSuccess = { AccountUiState.Success(it.firstOrNull()) },
                onFailure = { AccountUiState.Error(it) }
            )
        }
    }
}