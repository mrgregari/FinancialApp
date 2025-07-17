package com.example.feature_account.presentation


import androidx.lifecycle.viewModelScope
import com.example.core_domain.models.Account
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccountViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    //private val syncAccountsUseCase: SyncAccountsUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(networkState, errorHandler) {

    init {
        initializeNetworkState()
        loadAccount()
    }

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

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

    override fun onNetworkAvailable() {
        super.onNetworkAvailable()
        syncAccounts()
    }
    private fun syncAccounts() {
        viewModelScope.launch {
            //syncAccountUseCase()
        }
    }

    /*
    suspend fun syncAccounts() {
        val remoteAccounts = accountRemoteDataSource.getAccounts() // с сервера
        val localAccounts = accountDao.getAll() // из БД

        val localMap = localAccounts.associateBy { it.id }
        val remoteMap = remoteAccounts.associateBy { it.id }

        for ((id, remote) in remoteMap) {
            val local = localMap[id]
            if (local == null) {
                // На сервере есть, локально нет — добавить в БД
                accountDao.insertAll(listOf(remote.toEntity()))
            } else {
                when {
                    local.updatedAt > remote.updatedAt -> {
                        // Локальная новее — отправить на сервер
                        accountRemoteDataSource.updateAccount(
                            local.id,
                            UpdateAccountDto(local.name, local.balance, local.currency)
                        )
                        // Можно обновить isSynced = true
                    }
                    remote.updatedAt > local.updatedAt -> {
                        accountDao.insertAll(listOf(remote.toEntity()))
                    }
                }
            }
        }

    }

     */
}