package com.example.financialapp.data.repositories

import android.annotation.SuppressLint
import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.mappers.TransactionMapper
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.domain.models.Income
import com.example.financialapp.domain.repositories.TransactionRepository
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import java.util.TimeZone

class TransactionRepositoryImpl @Inject constructor(
    private val api: FinancialApi,
    private val mapper: TransactionMapper
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
    ): Result<List<Expense>> {
        return try {
            val (defaultStart, defaultEnd) = getDefaultPeriod()
            val start = startDate ?: defaultStart
            val end = endDate ?: defaultEnd
            val dtos = api.getTransactions(
                accountId = accountId,
                startDate = start.toApiString(),
                endDate = end.toApiString()
            )
            val transactions = dtos.map { mapper.fromDtoToExpense(it) }
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getIncomes(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): Result<List<Income>> {
        return try {
            val (defaultStart, defaultEnd) = getDefaultPeriod()
            val start = startDate ?: defaultStart
            val end = endDate ?: defaultEnd
            val dtos = api.getTransactions(
                accountId = accountId,
                startDate = start.toApiString(),
                endDate = end.toApiString()
            )
            val incomes = dtos.map { mapper.fromDtoToIncome(it) }
            Result.success(incomes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}