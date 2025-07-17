package com.example.core_domain.repositories

import com.example.core_domain.models.Account
import com.example.core_network.network.NetworkResult

/**
 * Repository for managing user accounts data.
 * Provides access to account information
 */
interface AccountRepository {
    /**
     * Retrieves all user accounts.
     *
     * @return List of accounts wrapped in NetworkResult
     */
    suspend fun getAccounts(): NetworkResult<List<Account>>

    suspend fun updateAccount(
        id: Int,
        name: String,
        balance: String,
        currency: String
    ): NetworkResult<Unit>

    suspend fun getAccountsFromRemote(): List<Account>
    suspend fun getAccountsFromLocal(): List<Account>
    suspend fun insertAccountToLocal(account: Account)
    suspend fun updateAccountRemote(account: Account)
}