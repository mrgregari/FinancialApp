package com.example.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core_ui.R


@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    emoji: String? = null,
    emojiBackgroundColor: Color = MaterialTheme.colorScheme.secondary,
    title: String,
    subTitle: String? = null,
    trailingText: String? = null,
    subTrailingText: String? = null,
    arrowIcon: Painter = painterResource(R.drawable.drill_in),
    showArrow: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
                } else {
                    Modifier
                }
            ),
        colors = ListItemDefaults.colors(
            containerColor = containerColor
        ),

        leadingContent = if (!emoji.isNullOrBlank()) {
            {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(emojiBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        style = if (emoji.any { it.isSurrogate() }) {
                            MaterialTheme.typography.bodyLarge
                        } else {
                            MaterialTheme.typography.labelSmall
                        },
                        color = Color.Black
                    )
                }
            }
        } else null,
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                    if (subTitle != null) {
                        Text(
                            text = subTitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    if (trailingText != null) {
                        Text(
                            text = trailingText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = textColor
                        )
                    }
                    if (subTrailingText != null) {
                        Text(
                            text = subTrailingText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = textColor,
                            maxLines = 1
                        )
                    }
                }
            }


        },
        trailingContent = if (showArrow) {
            {
                Icon(
                    painter = arrowIcon,
                    contentDescription = "Arrow",
                    tint = Color.Unspecified,
                )
            }
        } else null

    )
}
