package com.example.feature_incomes.presentation.todayIncomes


import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_domain.usecases.SyncCategoriesUseCase
import com.example.core_domain.usecases.SyncTransactionsUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.core_domain.usecases.GetIncomesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class IncomesViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getIncomesUseCase: GetIncomesUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    private val syncTransactionsUseCase: SyncTransactionsUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {


    private val _uiState = MutableStateFlow<IncomesUiState>(IncomesUiState.Loading)
    val uiState: StateFlow<IncomesUiState> = _uiState.asStateFlow()


    private val _currency = MutableStateFlow<String>("")

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        syncTransactions()
        loadIncomes(startDate = startDate)
        initializeNetworkState()
    }

    fun retry() {
        syncTransactions()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadIncomes(startDate = startDate)
    }

    private fun loadIncomes(startDate: Date? = null, endDate: Date? = null) {
        _uiState.value = IncomesUiState.Loading
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
                _uiState.value = IncomesUiState.Success(
                    incomes = incomes,
                    currency = _currency.value
                )
            },
            onError = { errorResId ->
                _uiState.value = IncomesUiState.Error(errorResId)
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

