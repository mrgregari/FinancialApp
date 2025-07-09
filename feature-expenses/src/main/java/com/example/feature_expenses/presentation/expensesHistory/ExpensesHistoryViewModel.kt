package com.example.financialapp.ui.screens.expensesHistory

import com.example.core_data.network.ErrorHandler
import com.example.core_data.network.NetworkResult
import com.example.core_data.network.NetworkState
import com.example.core_data.usecases.GetAccountUseCase
import com.example.core_ui.base.BaseViewModel
import com.example.financialapp.domain.usecases.GetExpensesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ExpensesHistoryViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {


    private val _startDate = MutableStateFlow<Date?>(null)
    val startDate: StateFlow<Date?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Date?>(null)
    val endDate: StateFlow<Date?> = _endDate.asStateFlow()

    private val _currency = MutableStateFlow<String>("")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _uiState = MutableStateFlow<ExpensesHistoryUiState>(ExpensesHistoryUiState.Loading)
    val uiState: StateFlow<ExpensesHistoryUiState> = _uiState.asStateFlow()

    init {
        val calendar = Calendar.getInstance()
        _endDate.value = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        _startDate.value = calendar.time
        loadAllExpenses()
        initializeNetworkState()
    }

    fun updateStartDate(date: Date?) {
        _startDate.value = date
        loadExpensesWithFilter()
    }

    fun updateEndDate(date: Date?) {
        _endDate.value = date
        loadExpensesWithFilter()
    }

    fun retry() {
        loadExpensesWithFilter()
    }

    private fun loadAllExpenses() {
        loadExpensesWithFilter()
    }

    private fun loadExpensesWithFilter() {
        _uiState.value = ExpensesHistoryUiState.Loading
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            _currency.value = account.currency
                            getExpensesUseCase(accountId, _startDate.value, _endDate.value)
                        } else {
                            NetworkResult.Error(Throwable("Нет доступного счёта"))
                        }
                    }

                    is NetworkResult.Error -> accountsResult
                    is NetworkResult.Loading -> NetworkResult.Loading
                }
            },
            onSuccess = { expenses ->
                _uiState.value = ExpensesHistoryUiState.Success(
                    expenses = expenses,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                    currency = _currency.value
                )
            },
            onError = { errorResId ->
                _uiState.value = ExpensesHistoryUiState.Error(errorResId)
            }
        )
    }
}