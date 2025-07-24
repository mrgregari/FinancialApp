package com.example.feature_settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import android.content.SharedPreferences
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheet(
    showColorSheet: Boolean,
    onDismiss: () -> Unit,
    colorOptions: List<Color>,
    prefs: SharedPreferences,
    onColorSelected: () -> Unit
) {
    if (showColorSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colorOptions.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable {
                                    prefs.edit().putInt("main_color", color.toArgb()).apply()
                                    onDismiss()
                                    onColorSelected()
                                }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
} 