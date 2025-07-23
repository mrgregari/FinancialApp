package com.example.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import androidx.compose.ui.platform.LocalDensity


@Composable
fun CategoryPieChart(
    data: List<PieChartEntity>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.sum }
    val percents = data.map { if (total == 0.0) 0f else (it.sum / total).toFloat() }
    val colors = listOf(
        Color(0xFF2AE881), Color(0xFFE4D662), Color(0xFFE46962), Color(0xFF62A3E4), Color(0xFFB662E4), Color(0xFFE4A662),
        Color(0xFF7ED957), Color(0xFF57D9C1), Color(0xFF5778D9), Color(0xFFD957B7), Color(0xFFD95757), Color(0xFFD9A857),
        Color(0xFF8E44AD), Color(0xFF16A085), Color(0xFFF39C12), Color(0xFF2C3E50), Color(0xFF27AE60), Color(0xFF2980B9),
        Color(0xFFC0392B), Color(0xFF34495E), Color(0xFF1ABC9C)
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val chartSize = 300.dp
        val padding = 24.dp
        val density = LocalDensity.current
        Canvas(
            modifier = Modifier
                .size(chartSize)
                .padding(padding)
                .pointerInput(data) {
                    detectTapGestures { offset ->
                        val center = Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val touchAngle = (Math.toDegrees(atan2(dy, dx).toDouble()) + 360) % 360
                        var startAngle = -90f
                        for ((i, percent) in percents.withIndex()) {
                            val sweep = percent * 360f
                            if (touchAngle >= startAngle && touchAngle < startAngle + sweep) {
                                selectedIndex = i
                                break
                            }
                            startAngle += sweep
                        }
                    }
                }
        ) {
            val minSize = size.minDimension
            val center = Offset(size.width / 2, size.height / 2)
            var startAngle = -90f
            percents.forEachIndexed { i, percent ->
                val sweep = percent * 360f
                drawArc(
                    color = colors[i % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = if (selectedIndex == i) minSize * 0.14f else minSize * 0.10f)
                )
                startAngle += sweep
            }
        }
        // Центр: название и процент
        val percentText = if (percents.isNotEmpty()) "${(percents[selectedIndex] * 100).toInt()}%" else "0%"
        androidx.compose.material3.Text(
            text = "$percentText ${data.getOrNull(selectedIndex)?.name ?: "-"}",
            style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        )
    }
} 