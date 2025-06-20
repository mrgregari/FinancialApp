package com.example.financialapp.domain.repositories

import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import java.util.Date

interface TransactionRepository {

    suspend fun getExpenses(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ) : Result<List<Expense>>

    suspend fun getIncomes(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ) : Result<List<Income>>
}