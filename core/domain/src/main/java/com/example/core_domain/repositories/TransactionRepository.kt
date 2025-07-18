package com.example.core_domain.repositories

import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_network.network.NetworkResult
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
    ): NetworkResult<List<Expense>>

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
    ): NetworkResult<List<Income>>

    suspend fun addExpense(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit>

    suspend fun addIncome(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit>

    suspend fun updateExpense(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit>

    suspend fun getExpenseById(id: Int) : NetworkResult<Expense>

    suspend fun deleteTransactionById(id: Int) : NetworkResult<Unit>

    suspend fun getIncomeById(id: Int) : NetworkResult<Income>

    suspend fun updateIncome(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit>

    suspend fun getExpensesLocal(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<Expense>

    suspend fun getIncomesLocal(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<Income>
}
