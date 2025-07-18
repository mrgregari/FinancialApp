package com.example.feature_expenses.presentation.todayExpenses

import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_domain.usecases.SyncCategoriesUseCase
import com.example.core_domain.usecases.SyncTransactionsUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.feature_expenses.domain.GetExpensesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val syncTransactionsUseCase: SyncTransactionsUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {

    init {
        initializeNetworkState()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        syncTransactions()
        loadExpenses(startDate = startDate)
    }

    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _currency = MutableStateFlow<String>("")


    fun retry() {
        syncTransactions()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadExpenses(startDate = startDate)
    }

    private fun loadExpenses(startDate: Date? = null, endDate: Date? = null) {
        safeApiCall(
            apiCall = {
                syncCategoriesUseCase()
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            _currency.value = account.currency
                            getExpensesUseCase(accountId, startDate, endDate)
                        } else {
                            NetworkResult.Error(Throwable("Нет доступного счёта"))
                        }
                    }

                    is NetworkResult.Error -> accountsResult
                    is NetworkResult.Loading -> NetworkResult.Loading
                }
            },
            onSuccess = { expenses ->
                _uiState.value = ExpensesUiState.Success(expenses, _currency.value)
            },
            onError = { errorResId ->
                _uiState.value = ExpensesUiState.Error(errorResId)
            }
        )
    }

    override fun onNetworkAvailable() {
        super.onNetworkAvailable()
        syncTransactions()
    }

    private fun syncTransactions() {
        viewModelScope.launch {
            syncTransactionsUseCase()
        }
    }
}