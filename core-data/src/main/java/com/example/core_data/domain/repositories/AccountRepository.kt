package com.example.core_data.domain.repositories

import com.example.core_data.domain.models.Account
import com.example.core_data.network.NetworkResult

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