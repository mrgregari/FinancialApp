package com.example.core_domain.usecases

import com.example.core_domain.repositories.TransactionRepository
import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        try {
            val accounts = accountRepository.getAccountsFromLocal()
            transactionRepository.syncTransactionsWithRemote(accounts)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}