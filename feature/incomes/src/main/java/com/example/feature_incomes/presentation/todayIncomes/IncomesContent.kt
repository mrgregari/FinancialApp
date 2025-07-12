package com.example.feature_incomes.presentation.todayIncomes

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.utils.formatAmountWithCurrency
import com.example.core_ui.utils.getCurrencySymbol
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomesContent(
    incomes: List<Income>,
    modifier: Modifier = Modifier,
    currency: String,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.total),
                subTitle = null,
                trailingText = formatAmountWithCurrency(
                    incomes.sumOf { it.amount.toDouble() },
                    currency = getCurrencySymbol(currency)
                ),
                subTrailingText = null,
                showArrow = false,
                containerColor = MaterialTheme.colorScheme.secondary
            )
            HorizontalDivider()
        }

        items(incomes) { income ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                title = income.title,
                emoji = income.icon,
                subTitle = income.comment,
                trailingText = formatAmountWithCurrency(income.amount.toDouble(), income.currency),
                subTrailingText = null,
                showArrow = true,
                onClick = { onItemClick(income.id) }
            )
            HorizontalDivider()
        }
    }
} 