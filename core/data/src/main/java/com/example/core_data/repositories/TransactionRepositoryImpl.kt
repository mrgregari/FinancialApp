package com.example.core_data.repositories

import android.annotation.SuppressLint
import com.example.core_data.remote.mappers.TransactionMapper
import com.example.core_data.remote.remoteDataSource.TransactionRemoteDataSource
import com.example.core_data.di.IODispatcher
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteDataSource: TransactionRemoteDataSource,
    private val mapper: TransactionMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {

    private fun getDefaultPeriod(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = calendar.time
        return startDate to endDate
    }

    @SuppressLint("SimpleDateFormat")
    private fun Date.toApiString(): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(this)
    }

    override suspend fun getExpenses(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): NetworkResult<List<Expense>> {
        return withContext(ioDispatcher) {
            try {
                val (defaultStart, defaultEnd) = getDefaultPeriod()
                val start = startDate ?: defaultStart
                val end = endDate ?: defaultEnd
                val dtos = transactionRemoteDataSource.getTransactions(
                    accountId = accountId,
                    startDate = start.toApiString(),
                    endDate = end.toApiString()
                )
                val expenses = dtos
                    .filter { it.category.isIncome == false }
                    .map { mapper.fromDtoToExpense(it) }
                    .sortedByDescending { it.date }
                NetworkResult.Success(expenses)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun getIncomes(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): NetworkResult<List<Income>> {
        return withContext(ioDispatcher) {
            try {
                val (defaultStart, defaultEnd) = getDefaultPeriod()
                val start = startDate ?: defaultStart
                val end = endDate ?: defaultEnd
                val dtos = transactionRemoteDataSource.getTransactions(
                    accountId = accountId,
                    startDate = start.toApiString(),
                    endDate = end.toApiString()
                )
                val incomes = dtos
                    .filter { it.category.isIncome == true }
                    .map { mapper.fromDtoToIncome(it) }
                    .sortedByDescending { it.date }
                NetworkResult.Success(incomes)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun addExpense(expense: Expense, accountId: Int, categoryId: Int): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromExpenseToCreateDto(expense, accountId, categoryId)
                val response = transactionRemoteDataSource.postTransaction(dto)
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(Exception("Ошибка при добавлении транзакции: ${response.code()}"))
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun addIncome(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromIncomeToCreateDto(income, accountId, categoryId)
                val response = transactionRemoteDataSource.postTransaction(dto)
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(Exception("Ошибка при добавлении транзакции: ${response.code()}"))
                }
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

}