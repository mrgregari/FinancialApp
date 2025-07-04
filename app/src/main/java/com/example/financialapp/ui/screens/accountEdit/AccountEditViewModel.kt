package com.example.financialapp.ui.screens.accountEdit

import androidx.lifecycle.viewModelScope
import com.example.financialapp.R
import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
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

    fun updateAccount(accountId: Int, name: String, balance: String, currency: String) {
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