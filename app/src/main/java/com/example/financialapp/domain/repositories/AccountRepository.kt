package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Account

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
}