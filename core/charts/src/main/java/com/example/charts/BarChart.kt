package com.example.charts


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.delay


@Composable
fun BarChart(
    incomes: List<BarChartEntity>,
    expenses: List<BarChartEntity>,
    modifier: Modifier = Modifier,
    barSpacing: Float = 16f,
    sidePaddingDp: Float = 32f,
    topPaddingDp: Float = 24f
) {
    val barCount = expenses.size
    val maxExpense = expenses.maxOfOrNull { it.amount }?.toFloat() ?: 1f

    if (barCount == 0) return

    val textColor = MaterialTheme.colorScheme.onSurface
    var selectedBarIndex by remember { mutableStateOf<Int?>(null) }
    var chartVisible by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (chartVisible) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800), label = "barChartProgress"
    )
    LaunchedEffect(Unit) {
        delay(100)
        chartVisible = true
    }

    Canvas(
        modifier = modifier.pointerInput(barCount) {
            detectTapGestures { offset ->
                val density = this@pointerInput.density
                val chartWidth = size.width
                val sidePadding = sidePaddingDp * density
                val totalSpacing = barSpacing * (barCount - 1)
                val barWidth = (chartWidth - totalSpacing - 2 * sidePadding) / barCount

                for (i in 0 until barCount) {
                    val left = sidePadding + i * (barWidth + barSpacing)
                    val right = left + barWidth
                    if (offset.x in left..right) {
                        selectedBarIndex = if (selectedBarIndex == i) null else i
                        break
                    }
                }
            }
        }
    ) {
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
            val barColor =
                if (income > expense) Color(0xFF2AE881) else Color(0xFFE46962)

            val left = sidePadding + i * (barWidth + barSpacing)
            val barHeight = if (maxExpense == 0f) 0f else (expense / maxExpense) * availableHeight * progress
            val top = chartHeight - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top - topPadding),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 4, barWidth / 4)
            )

            // Если этот столбец выбран — рисуем дату под ним
            if (selectedBarIndex == i) {
                val date = expenses[i].date
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        color = textColor.toArgb()
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    canvas.nativeCanvas.drawText(
                        date,
                        left + barWidth / 2,
                        chartHeight - 4,
                        paint
                    )
                }
            }
        }
    }
}