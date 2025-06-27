package com.example.financialapp.ui.screens.expensesHistory

import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.GetExpensesUseCase
import com.example.financialapp.ui.base.BaseViewModel
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


    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _startDate = MutableStateFlow<Date?>(null)
    val startDate: StateFlow<Date?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Date?>(null)
    val endDate: StateFlow<Date?> = _endDate.asStateFlow()

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
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
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
                _expenses.value = expenses
            }
        )
    }
}