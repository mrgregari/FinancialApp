package com.example.financialapp.ui.screens.account

import com.example.financialapp.domain.models.Account

sealed class AccountUiState {

    object Loading : AccountUiState()
    data class Success(val account: Account?) : AccountUiState()
    data class Error(val throwable: Throwable) : AccountUiState()

}