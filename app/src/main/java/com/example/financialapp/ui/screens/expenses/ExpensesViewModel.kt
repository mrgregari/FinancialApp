package com.example.financialapp.ui.screens.expenses

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


class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
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
        loadExpenses(startDate = startDate)
    }

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _currency = MutableStateFlow<String>("")
    val currency: StateFlow<String> = _currency.asStateFlow()


    fun retry() {
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
                _expenses.value = expenses
            }
        )
    }
}