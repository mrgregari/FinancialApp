package com.example.financialapp.ui.screens.accountEdit

import com.example.financialapp.domain.models.Account


sealed class AccountEditUiState {
    object Loading : AccountEditUiState()
    data class Success(val account: Account) : AccountEditUiState()
    data class Error(val errorResId: Int) : AccountEditUiState()
    object Updated : AccountEditUiState()
}