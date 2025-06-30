package com.example.financialapp.data.repositories

import android.annotation.SuppressLint
import com.example.financialapp.data.remote.remoteDataSource.TransactionRemoteDataSource
import com.example.financialapp.data.mappers.TransactionMapper
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.di.IODispatcher
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import com.example.financialapp.domain.repositories.TransactionRepository
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
}