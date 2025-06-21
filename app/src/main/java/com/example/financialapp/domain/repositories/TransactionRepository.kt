package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import java.util.Date

interface TransactionRepository {

    suspend fun getExpenses(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ) : NetworkResult<List<Expense>>

    suspend fun getIncomes(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ) : NetworkResult<List<Income>>
}