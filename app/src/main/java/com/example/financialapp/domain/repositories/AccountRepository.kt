package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Account

interface AccountRepository {
    suspend fun getAccounts(): NetworkResult<List<Account>>
}