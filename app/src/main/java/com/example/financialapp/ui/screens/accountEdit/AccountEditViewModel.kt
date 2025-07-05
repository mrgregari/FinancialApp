package com.example.financialapp.ui.screens.accountEdit

import androidx.lifecycle.viewModelScope
import com.example.financialapp.R
import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Account
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.UpdateAccountUseCase
import com.example.financialapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountEditViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {

    private val _uiState = MutableStateFlow<AccountEditUiState>(AccountEditUiState.Loading)
    val uiState: StateFlow<AccountEditUiState> = _uiState

    private var currentAccountId: Int? = null
    private var currentAccount: Account? = null

    init {
        initializeNetworkState()
    }

    fun loadAccount(accountId: Int) {
        currentAccountId = accountId
        _uiState.value = AccountEditUiState.Loading
        viewModelScope.launch {
            safeApiCall(
                apiCall = { getAccountUseCase() },
                onSuccess = { accounts ->
                    val account = accounts.firstOrNull { it.id == accountId }
                    if (account != null) {
                        currentAccount = account
                        _uiState.value = AccountEditUiState.Success(account)
                    } else {
                        _uiState.value = AccountEditUiState.Error(R.string.account_not_found)
                    }
                },
                onError = { errorResId ->
                    _uiState.value = AccountEditUiState.Error(errorResId)
                }
            )
        }
    }

    fun validateField(field: String, value: String) {
        val currentState = _uiState.value
        if (currentState is AccountEditUiState.Success) {
            val validationState = when (field) {
                "name" -> {
                    val nameError = AccountValidator.validateName(value)?.getMessage()
                    currentState.validationState.copy(nameError = nameError)
                }
                "balance" -> {
                    val balanceError = AccountValidator.validateBalance(value)?.getMessage()
                    currentState.validationState.copy(balanceError = balanceError)
                }
                "currency" -> {
                    val currencyError = AccountValidator.validateCurrency(value)?.getMessage()
                    currentState.validationState.copy(currencyError = currencyError)
                }
                else -> currentState.validationState
            }

            val updatedValidationState = validationState.copy(
                isFormValid = !validationState.hasErrors()
            )
            
            _uiState.value = currentState.copy(validationState = updatedValidationState)
        }
    }

    fun validateAllFields(name: String, balance: String, currency: String) {
        val currentState = _uiState.value
        if (currentState is AccountEditUiState.Success) {
            val validationState = AccountValidator.validateAll(name, balance, currency)
            _uiState.value = currentState.copy(validationState = validationState)
        }
    }

    fun updateAccount(accountId: Int, name: String, balance: String, currency: String) {
        val validationState = AccountValidator.validateAll(name, balance, currency)
        if (!validationState.isFormValid) {
            val currentState = _uiState.value
            if (currentState is AccountEditUiState.Success) {
                _uiState.value = currentState.copy(validationState = validationState)
            }
            return
        }

        _uiState.value = AccountEditUiState.Loading
        viewModelScope.launch {
            safeApiCall(
                apiCall = { updateAccountUseCase(accountId, name, balance, currency) },
                onSuccess = {
                    _uiState.value = AccountEditUiState.Updated
                },
                onError = { errorResId ->
                    _uiState.value = AccountEditUiState.Error(errorResId)
                }
            )
        }
    }
}