package com.example.financialapp.ui.screens.accountEdit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.example.financialapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column {
            CurrencySheetItem(
                iconRes = R.drawable.ruble,
                text = "Российский рубль ₽",
                onClick = {
                    onCurrencySelected("RUB")
                    onDismiss()
                }
            )
            HorizontalDivider()
            CurrencySheetItem(
                iconRes = R.drawable.dollar,
                text = "Американский доллар $",
                onClick = {
                    onCurrencySelected("USD")
                    onDismiss()
                }
            )
            HorizontalDivider()
            CurrencySheetItem(
                iconRes = R.drawable.euro,
                text = "Евро €",
                onClick = {
                    onCurrencySelected("EUR")
                    onDismiss()
                }
            )
            HorizontalDivider()
            CancelSheetItem(onClick = onDismiss)
        }
    }
}

