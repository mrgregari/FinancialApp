package com.example.financialapp.ui.screens.accountEdit.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financialapp.ui.components.CustomListItem

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
        EditAccountField(
            label = "Название счета",
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
        EditAccountField(
            label = "Баланс",
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
            title = "Валюта",
            trailingText = currency,
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
    }
}