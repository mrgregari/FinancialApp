package com.example.feature_account.presentation.accountEdit

import com.example.core_data.domain.models.Account

sealed class AccountEditUiState {
    object Loading : AccountEditUiState()
    data class Success(
        val account: Account,
        val validationState: ValidationState = ValidationState()
    ) : AccountEditUiState()
    data class Error(val errorResId: Int) : AccountEditUiState()
    object Updated : AccountEditUiState()
}

data class ValidationState(
    val nameError: String? = null,
    val balanceError: String? = null,
    val currencyError: String? = null,
    val isFormValid: Boolean = false
) {
    fun hasErrors(): Boolean = nameError != null || balanceError != null || currencyError != null
}

sealed class ValidationError {
    object EmptyName : ValidationError()
    object EmptyBalance : ValidationError()
    object InvalidBalance : ValidationError()
    object EmptyCurrency : ValidationError()
    
    fun getMessage(): String = when (this) {
        is EmptyName -> "Имя не может быть пустым"
        is EmptyBalance -> "Баланс не может быть пустым"
        is InvalidBalance -> "Введите корректное число"
        is EmptyCurrency -> "Выберите валюту"
    }
}