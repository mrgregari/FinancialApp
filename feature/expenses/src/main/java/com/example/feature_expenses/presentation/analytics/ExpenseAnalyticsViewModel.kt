package com.example.feature_expenses.presentation.analytics

import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.feature_expenses.domain.GetExpensesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ExpenseAnalyticsViewModel @Inject constructor(
    networkState: NetworkState,
    errorHandler: ErrorHandler,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
) : BaseViewModel(networkState, errorHandler) {

    private val _uiState =
        MutableStateFlow<ExpensesAnalyticsUiState>(ExpensesAnalyticsUiState.Loading)
    val uiState: StateFlow<ExpensesAnalyticsUiState> = _uiState.asStateFlow()

    private val _startDate = MutableStateFlow<Date?>(null)

    private val _endDate = MutableStateFlow<Date?>(null)

    private val _currency = MutableStateFlow<String>("")

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
        _uiState.value = ExpensesAnalyticsUiState.Loading
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
                val group = expenses.groupBy { it.title }
                val total = expenses.sumOf { it.amount.toDouble() }
                _uiState.value = ExpensesAnalyticsUiState.Success(
                    total = total,
                    expenses = group,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                    currency = _currency.value
                )
            },
            onError = { errorResId ->
                _uiState.value = ExpensesAnalyticsUiState.Error(errorResId)
            }
        )
    }
}