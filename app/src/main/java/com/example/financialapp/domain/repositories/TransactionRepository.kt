package com.example.financialapp.domain.repositories

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import java.util.Date

/**
 * Repository for managing transaction data.
 * Provides access to expenses and incomes information
 */
interface TransactionRepository {

    /**
     * Retrieves expenses for a specific account.
     * 
     * @param accountId ID of the account
     * @param startDate Optional start date filter
     * @param endDate Optional end date filter
     * @return List of expenses wrapped in NetworkResult
     */
    suspend fun getExpenses(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ) : NetworkResult<List<Expense>>

    /**
     * Retrieves incomes for a specific account.
     * 
     * @param accountId ID of the account
     * @param startDate Optional start date filter
     * @param endDate Optional end date filter
     * @return List of incomes wrapped in NetworkResult
     */
    suspend fun getIncomes(
        accountId: Int,
        startDate: Date? = null,
        endDate: Date? = null
    ) : NetworkResult<List<Income>>
}