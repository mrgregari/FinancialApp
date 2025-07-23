package com.example.charts


import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.CornerRadius


@Composable
fun BarChart(
    incomes: List<BarChartEntity>,
    expenses: List<BarChartEntity>,
    modifier: Modifier = Modifier,
    barSpacing: Float = 16f,      // увеличенное расстояние между столбцами
    sidePaddingDp: Float = 32f,   // одинаковый отступ слева и справа
    topPaddingDp: Float = 24f     // отступ сверху
) {
    val barCount = expenses.size
    val maxExpense = expenses.maxOfOrNull { it.amount }?.toFloat() ?: 1f

    if (barCount == 0) return

    Canvas(modifier = modifier) {
        val chartHeight = size.height
        val chartWidth = size.width
        val density = this@Canvas.density

        val sidePadding = sidePaddingDp * density
        val topPadding = topPaddingDp * density

        val availableHeight = chartHeight - topPadding
        val totalSpacing = barSpacing * (barCount - 1)
        val barWidth = (chartWidth - totalSpacing - 2 * sidePadding) / barCount

        for (i in 0 until barCount) {
            val expense = expenses[i].amount.toFloat()
            val income = incomes[i].amount.toFloat()
            val color =
                if (income > expense) Color(0xFF2AE881) else Color(0xFFE46962)

            val left = sidePadding + i * (barWidth + barSpacing)
            val barHeight = if (maxExpense == 0f) 0f else (expense / maxExpense) * availableHeight
            val top = chartHeight - barHeight

            drawRoundRect(
                color = color,
                topLeft = Offset(left, top - topPadding),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 4, barWidth / 4)
            )
        }
    }
}