package com.example.feature_account.presentation.accountEdit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.components.EditField
import com.example.core_ui.utils.getCurrencySymbol
import com.example.core_ui.R

@Composable
fun AccountEditContent(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String?,
    balance: String,
    onValueChange: (String) -> Unit,
    balanceError: String?,
    currency: String,
    onCurrencyClick: () -> Unit,
    currencyError: String?
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        EditField(
            label = stringResource(R.string.account_name),
            value = name,
            onValueChange = onNameChange
        )
        if (nameError != null) {
            Text(
                text = nameError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
            )
        }
        EditField(
            label = stringResource(R.string.balance),
            value = balance,
            onValueChange = onValueChange,
            keyboardType = KeyboardType.Number
        )
        if (balanceError != null) {
            Text(
                text = balanceError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
            )
        }
        CustomListItem(
            modifier = Modifier.height(71.dp),
            title = stringResource(R.string.currency),
            trailingText = getCurrencySymbol(currency),
            showArrow = true,
            onClick = onCurrencyClick
        )
        if (currencyError != null) {
            Text(
                text = currencyError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        HorizontalDivider()
    }
}