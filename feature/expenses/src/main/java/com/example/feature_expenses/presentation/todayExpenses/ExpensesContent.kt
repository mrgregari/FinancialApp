package com.example.feature_expenses.presentation.todayExpenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core_domain.models.Expense
import com.example.core_ui.R
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.utils.formatAmountWithCurrency
import com.example.core_ui.utils.getCurrencySymbol

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpensesContent(
    expenses: List<Expense>,
    currency: String,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.total),
                subTitle = null,
                trailingText = formatAmountWithCurrency(
                    expenses.sumOf { it.amount.toDouble() },
                    currency = getCurrencySymbol(currency)
                ),
                subTrailingText = null,
                showArrow = false,
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider()
        }

        items(expenses) { expense ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = expense.icon,
                title = expense.title,
                subTitle = expense.comment,
                trailingText = formatAmountWithCurrency(
                    expense.amount.toDouble(),
                    getCurrencySymbol(expense.currency)
                ),
                showArrow = true,
                onClick = { onItemClick(expense.id) }
            )
            HorizontalDivider()
        }
    }
}