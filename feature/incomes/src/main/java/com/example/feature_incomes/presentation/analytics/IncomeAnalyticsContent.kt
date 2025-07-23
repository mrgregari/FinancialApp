package com.example.feature_incomes.presentation.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core_domain.models.Income
import com.example.core_ui.components.CustomDatePickerDialog
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.utils.formatAmountWithCurrency
import com.example.core_ui.utils.getCurrencySymbol
import com.example.charts.PieChartEntity
import com.example.charts.CategoryPieChart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncomeAnalyticsContent(
    incomes: Map<String, List<Income>>,
    startDate: Date?,
    endDate: Date?,
    currency: String,
    total: Double,
    showStartDatePicker: Boolean,
    showEndDatePicker: Boolean,
    onShowStartDatePicker: () -> Unit,
    onShowEndDatePicker: () -> Unit,
    onDismissStartDatePicker: () -> Unit,
    onDismissEndDatePicker: () -> Unit,
    onStartDateSelected: (Date?) -> Unit,
    onEndDateSelected: (Date?) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }

    Column(modifier = Modifier) {
        // Период: начало
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Период: начало",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                    .clickable { onShowStartDatePicker() }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = startDate?.let { dateFormat.format(it) } ?: "Не выбрано",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        HorizontalDivider()
        // Период: конец
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Период: конец",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                    .clickable { onShowEndDatePicker() }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = endDate?.let { dateFormat.format(it) } ?: "Не выбрано",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        HorizontalDivider()
        CustomListItem(
            title = "Сумма",
            trailingText = formatAmountWithCurrency(total, getCurrencySymbol(currency))
        )
        val pieChartData = incomes.map { (category, list) ->
            PieChartEntity(
                name = category,
                sum = list.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            )
        }
        HorizontalDivider()
        LazyColumn {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CategoryPieChart(
                        data = pieChartData,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                HorizontalDivider()
            }
            items(incomes.entries.toList()) { entry ->
                val sum = entry.value.sumOf { it.amount.toDouble() }
                CustomListItem(
                    modifier = Modifier.height(71.dp),
                    title = entry.key,
                    emoji = entry.value.first().icon,
                    trailingText = "${String.format(Locale.US, "%.1f", sum / total * 100)} %",
                    subTrailingText = formatAmountWithCurrency(sum, getCurrencySymbol(currency))
                )
                HorizontalDivider()
            }
        }
    }

    // Диалоги выбора дат
    CustomDatePickerDialog(
        showDialog = showStartDatePicker,
        onDismiss = onDismissStartDatePicker,
        onDateSelected = onStartDateSelected
    )
    CustomDatePickerDialog(
        showDialog = showEndDatePicker,
        onDismiss = onDismissEndDatePicker,
        onDateSelected = onEndDateSelected
    )
} 