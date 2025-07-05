package com.example.financialapp.ui.screens.accountEdit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.financialapp.R

@Composable
fun CancelSheetItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(71.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onError)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.cancel_icon),
            tint = MaterialTheme.colorScheme.surfaceContainer,
            contentDescription = "Отмена",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Отмена",
            color = MaterialTheme.colorScheme.surfaceContainer,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}