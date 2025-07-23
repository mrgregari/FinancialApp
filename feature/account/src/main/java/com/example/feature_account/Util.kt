package com.example.feature_account

import com.example.charts.BarChartEntity
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun alignBarChartEntities(
    incomes: List<BarChartEntity>,
    expenses: List<BarChartEntity>
): Pair<List<BarChartEntity>, List<BarChartEntity>> {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Преобразуем даты к формату "день-месяц-год" и группируем суммы по дню
    fun groupByDay(entities: List<BarChartEntity>): Map<String, Double> =
        entities
            .map {
                val parsed = ZonedDateTime.parse(it.date)
                val day = parsed.format(formatter)
                day to it.amount
            }
            .groupBy({ it.first }, { it.second })
            .mapValues { (_, amounts) -> amounts.sum() }

    val incomeByDay = groupByDay(incomes)
    val expenseByDay = groupByDay(expenses)

    // Собираем все уникальные дни
    val allDays = (incomeByDay.keys + expenseByDay.keys).toSortedSet()

    // Формируем выровненные списки
    val alignedIncomes = allDays.map { day ->
        BarChartEntity(day, incomeByDay[day] ?: 0.0)
    }
    val alignedExpenses = allDays.map { day ->
        BarChartEntity(day, expenseByDay[day] ?: 0.0)
    }

    return alignedIncomes to alignedExpenses
}