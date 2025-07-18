package com.example.core_domain.usecases

import com.example.core_domain.repositories.TransactionRepository
import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.content.SharedPreferences

class SyncTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val syncPrefs: SharedPreferences
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        try {
            val accounts = accountRepository.getAccountsFromLocal()
            val result = transactionRepository.syncTransactionsWithRemote(accounts)
            if (result is NetworkResult.Success) {
                syncPrefs.edit().putLong("last_sync_time", System.currentTimeMillis()).apply()
            }
            result
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}