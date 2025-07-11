package com.example.feature_account.presentation.accountEdit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.example.core_ui.R
import com.example.core_ui.components.BottomSheetItem
import com.example.core_ui.components.CancelSheetItem

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
            BottomSheetItem(
                iconRes = R.drawable.ruble,
                text = "Российский рубль ₽",
                onClick = {
                    onCurrencySelected("RUB")
                    onDismiss()
                }
            )
            HorizontalDivider()
            BottomSheetItem(
                iconRes = R.drawable.dollar,
                text = "Американский доллар $",
                onClick = {
                    onCurrencySelected("USD")
                    onDismiss()
                }
            )
            HorizontalDivider()
            BottomSheetItem(
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

