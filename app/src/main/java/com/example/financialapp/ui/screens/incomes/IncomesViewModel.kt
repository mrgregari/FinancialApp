package com.example.financialapp.ui.screens.incomes

import androidx.lifecycle.viewModelScope
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.domain.models.Income
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.GetIncomesUseCase
import com.example.financialapp.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class IncomesViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getIncomesUseCase: GetIncomesUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
): BaseViewModel() {

    init {
        this.networkState = networkState
        this.errorHandler = errorHandler
        initializeNetworkState()
    }

    private val _incomes = MutableStateFlow<List<Income>>(emptyList())
    val incomes: StateFlow<List<Income>> = _incomes.asStateFlow()

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadIncomes(startDate = startDate)
    }

    fun retry() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadIncomes(startDate = startDate)
    }

    private fun loadIncomes(startDate: Date? = null, endDate: Date? = null) {
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            getIncomesUseCase(accountId, startDate, endDate)
                        } else {
                            NetworkResult.Error(Throwable("Нет доступного счёта"))
                        }
                    }
                    is NetworkResult.Error -> accountsResult
                    is NetworkResult.Loading -> NetworkResult.Loading
                }
            },
            onSuccess = { incomes ->
                _incomes.value = incomes
            }
        )
    }
}