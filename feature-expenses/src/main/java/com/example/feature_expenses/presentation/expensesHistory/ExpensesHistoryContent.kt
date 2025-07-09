package com.example.financialapp.ui.screens.expensesHistory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core_data.domain.models.Expense
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomDatePickerDialog
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.utils.formatAmountWithCurrency
import com.example.financialapp.ui.utils.formatDate
import com.example.financialapp.ui.utils.formatDateTime
import com.example.financialapp.ui.utils.getCurrencySymbol

@Composable
fun ExpensesHistoryContent(
    expenses: List<Expense>,
    startDate: java.util.Date?,
    endDate: java.util.Date?,
    currency: String,
    showStartDatePicker: Boolean,
    showEndDatePicker: Boolean,
    onShowStartDatePicker: () -> Unit,
    onShowEndDatePicker: () -> Unit,
    onDismissStartDatePicker: () -> Unit,
    onDismissEndDatePicker: () -> Unit,
    onStartDateSelected: (java.util.Date?) -> Unit,
    onEndDateSelected: (java.util.Date?) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            Column {
                CustomListItem(
                    modifier = Modifier.height(56.dp),
                    title = stringResource(R.string.start),
                    trailingText =
                        startDate?.let { formatDate(it) } ?: stringResource(R.string.choose_date),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onClick = onShowStartDatePicker
                )
                HorizontalDivider()
                CustomListItem(
                    modifier = Modifier.height(56.dp),
                    title = "Конец",
                    trailingText = endDate?.let { formatDate(it) } ?: "Выберите дату",
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onClick = onShowEndDatePicker
                )
                HorizontalDivider()
                CustomListItem(
                    modifier = Modifier.height(56.dp),
                    title = "Сумма",
                    trailingText = formatAmountWithCurrency(
                        expenses.sumOf { it.amount.toDouble() },
                        getCurrencySymbol(currency)
                    ),
                    showArrow = false,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
        items(expenses) { expense ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = expense.icon,
                title = expense.title,
                subTitle = expense.comment,
                trailingText = formatAmountWithCurrency(expense.amount.toDouble(), expense.currency),
                subTrailingText = formatDateTime(expense.date),
                showArrow = true,
            )
            HorizontalDivider()
        }
    }
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