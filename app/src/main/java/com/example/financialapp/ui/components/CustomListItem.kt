package com.example.financialapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financialapp.R
import com.example.financialapp.ui.theme.FinancialAppTheme


@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    emoji: String? = null,
    title: String,
    subTitle: String? = null,
    trailingText: String? = null,
    subTrailingText: String? = null,
    showArrow: Boolean = false
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = containerColor
        ),

        leadingContent = if (!emoji.isNullOrBlank()) {
            {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            }
        } else null,
        headlineContent = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subTitle != null) {
                        Text(
                            text = subTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    if (trailingText != null) {
                        Text(
                            text = trailingText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            )
                    }
                    if (subTrailingText != null) {
                        Text(
                            text = subTrailingText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }


        },
        trailingContent = if (showArrow) {
             {
                Icon(
                    painter = painterResource(R.drawable.drill_in),
                    contentDescription = "Arrow",
                    tint = Color.Unspecified,
                )
            }
        }
        else null

    )
}

@Preview
@Composable
fun CustomListItemPreview() {
    FinancialAppTheme(dynamicColor = false) {
        CustomListItem(
            Modifier,
            emoji = "\uD83C\uDFE0",
            title = "Ремонт квартиры",
            subTitle = "Ремонт - фурнитура для дверей",
            trailingText = "100 000$",
            subTrailingText = "80%",
            showArrow = true
        )
    }

}