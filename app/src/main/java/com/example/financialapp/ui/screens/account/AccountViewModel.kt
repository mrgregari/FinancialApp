package com.example.financialapp.ui.screens.account

import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Account
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel() {

    init {
        this.networkState = networkState
        this.errorHandler = errorHandler
        initializeNetworkState()
    }

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    init {
        loadAccount()
    }

    fun retry() {
        loadAccount()
    }

    private fun loadAccount() {
        safeApiCall(
            apiCall = { getAccountUseCase() },
            onSuccess = { accounts ->
                _accounts.value = accounts
            }
        )
    }
}