package com.example.feature_account.presentation


import androidx.lifecycle.viewModelScope
import com.example.core_domain.models.Account
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_domain.usecases.GetExpensesUseCase
import com.example.core_domain.usecases.GetIncomesUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.feature_account.domain.SyncAccountsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val syncAccountsUseCase: SyncAccountsUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getIncomesUseCase: GetIncomesUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {

    init {
        initializeNetworkState()
        loadAccount()
    }

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()


    private val _incomes = MutableStateFlow<List<Income>>(emptyList())
    val incomes: StateFlow<List<Income>> = _incomes.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()


    fun retry() {
        syncAccounts()
        loadAccount()
    }

    private fun loadAccount() {
        safeApiCall(
            apiCall = { getAccountUseCase() },
            onSuccess = { accounts ->
                _accounts.value = accounts
                loadExpenses(_accounts.value.first().id)
                loadIncomes(_accounts.value.first().id)
            }
        )
    }

    private fun loadExpenses(accountId: Int) {
        safeApiCall(
            apiCall = { getExpensesUseCase(accountId) },
            onSuccess = { expenses ->
                _expenses.value = expenses
            }
        )
    }

    private fun loadIncomes(accountId: Int) {
        safeApiCall(
            apiCall = { getIncomesUseCase(accountId) },
            onSuccess = { incomes ->
                _incomes.value = incomes
            }
        )
    }

    override fun onNetworkAvailable() {
        super.onNetworkAvailable()
        syncAccounts()
    }

    private fun syncAccounts() {
        viewModelScope.launch {
            syncAccountsUseCase()
        }
    }

}