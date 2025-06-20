package com.example.financialapp.domain.repositories

import com.example.financialapp.domain.models.Account

interface AccountRepository {

    suspend fun getAllAccounts() : Result<List<Account>>
}