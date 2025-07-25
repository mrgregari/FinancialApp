package com.example.feature_incomes.presentation.incomesHistory


import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_domain.usecases.SyncTransactionsUseCase
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.core_domain.usecases.GetIncomesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.launch


class IncomesHistoryViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val syncTransactionsUseCase: SyncTransactionsUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {


    private val _uiState = MutableStateFlow<IncomesHistoryUiState>(IncomesHistoryUiState.Loading)
    val uiState: StateFlow<IncomesHistoryUiState> = _uiState.asStateFlow()

    private val _startDate = MutableStateFlow<Date?>(null)

    private val _endDate = MutableStateFlow<Date?>(null)

    private val _currency = MutableStateFlow<String>("")

    init {
        syncTransactions()
        val calendar = Calendar.getInstance()
        _endDate.value = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        _startDate.value = calendar.time
        loadAllIncomes()
        initializeNetworkState()
    }

    fun updateStartDate(date: Date?) {
        _startDate.value = date
        loadIncomesWithFilter()
    }

    fun updateEndDate(date: Date?) {
        _endDate.value = date
        loadIncomesWithFilter()
    }

    fun retry() {
        syncTransactions()
        loadIncomesWithFilter()
    }

    private fun loadAllIncomes() {
        loadIncomesWithFilter()
    }

    private fun loadIncomesWithFilter() {
        _uiState.value = IncomesHistoryUiState.Loading
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            _currency.value = account.currency
                            getIncomesUseCase(accountId, _startDate.value, _endDate.value)
                        } else {
                            NetworkResult.Error(Throwable("Нет доступного счёта"))
                        }
                    }
                    is NetworkResult.Error -> accountsResult
                    is NetworkResult.Loading -> NetworkResult.Loading
                }
            },
            onSuccess = { incomes ->
                _uiState.value = IncomesHistoryUiState.Success(
                    incomes = incomes,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                    currency = _currency.value
                )
            },
            onError = { errorResId ->
                _uiState.value = IncomesHistoryUiState.Error(errorResId)
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