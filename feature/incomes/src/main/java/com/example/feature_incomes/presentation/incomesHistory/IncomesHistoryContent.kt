package com.example.feature_incomes.presentation.incomesHistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core_domain.models.Income
import com.example.core_ui.R
import com.example.core_ui.components.CustomDatePickerDialog
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.utils.formatAmountWithCurrency
import com.example.core_ui.utils.formatDate
import com.example.core_ui.utils.formatDateTime
import com.example.core_ui.utils.getCurrencySymbol
import java.util.Date
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomesHistoryContent(
    incomes: List<Income>,
    startDate: Date?,
    endDate: Date?,
    currency: String,
    showStartDatePicker: Boolean,
    showEndDatePicker: Boolean,
    onShowStartDatePicker: () -> Unit,
    onShowEndDatePicker: () -> Unit,
    onDismissStartDatePicker: () -> Unit,
    onDismissEndDatePicker: () -> Unit,
    onStartDateSelected: (Date?) -> Unit,
    onEndDateSelected: (Date?) -> Unit
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
                    title = stringResource(R.string.end),
                    trailingText =
                        endDate?.let { formatDate(it) } ?: stringResource(R.string.choose_date),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    onClick = onShowEndDatePicker
                )
                HorizontalDivider()
                CustomListItem(
                    modifier = Modifier.height(56.dp),
                    title = stringResource(R.string.amount),
                    trailingText = formatAmountWithCurrency(
                        incomes.sumOf { it.amount.toDouble() },
                        getCurrencySymbol(currency)
                    ),
                    showArrow = false,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
        items(incomes) { income ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = income.icon,
                title = income.title,
                subTitle = income.comment,
                trailingText = formatAmountWithCurrency(income.amount.toDouble(), income.currency),
                subTrailingText = formatDateTime(income.date),
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