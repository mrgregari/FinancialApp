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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay


@Composable
fun CategoryPieChart(
    data: List<PieChartEntity>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.sum }
    val percents = data.map { if (total == 0.0) 0f else (it.sum / total).toFloat() }
    val colors = listOf(
        Color(0xFF2AE881),
        Color(0xFFE4D662),
        Color(0xFFE46962),
        Color(0xFF62A3E4),
        Color(0xFFB662E4),
        Color(0xFFE4A662),
        Color(0xFF7ED957),
        Color(0xFF57D9C1),
        Color(0xFF5778D9),
        Color(0xFFD957B7),
        Color(0xFFD95757),
        Color(0xFFD9A857),
        Color(0xFF8E44AD),
        Color(0xFF16A085),
        Color(0xFFF39C12),
        Color(0xFF2C3E50),
        Color(0xFF27AE60),
        Color(0xFF2980B9),
        Color(0xFFC0392B),
        Color(0xFF34495E),
        Color(0xFF1ABC9C)
    )
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var chartVisible by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (chartVisible) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800),
        label = "pieChartProgress"
    )
    LaunchedEffect(Unit) {
        delay(100)
        chartVisible = true
    }

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
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = minOf(size.width, size.height) / 2f
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                        if (distance > radius) {
                            selectedIndex = null
                            return@detectTapGestures
                        }
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
                val sweep = percent * 360f * progress
                drawArc(
                    color = colors[i % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = if (selectedIndex == i) minSize * 0.14f else minSize * 0.10f)
                )
                startAngle += percent * 360f
            }
        }

        val percentText =
            if (selectedIndex != null && percents.isNotEmpty()) "${(percents[selectedIndex!!] * 100).toInt()}%" else ""
        val categoryName =
            if (selectedIndex != null) data.getOrNull(selectedIndex!!)?.name ?: "" else ""
        if (selectedIndex != null) {
            androidx.compose.material3.Text(
                text = "$percentText $categoryName",
                style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
} 