package com.example.feature_settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.SharedPreferences
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HapticSheet(
    showHapticSheet: Boolean,
    onDismiss: () -> Unit,
    hapticOptions: List<String>,
    selectedHaptic: Int,
    prefs: SharedPreferences
) {
    if (showHapticSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.padding(16.dp)) {
                hapticOptions.forEachIndexed { idx, label ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                prefs.edit().putInt("haptic_mode", idx).apply()
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedHaptic == idx,
                            onClick = {
                                prefs.edit().putInt("haptic_mode", idx).apply()
                                onDismiss()
                            }
                        )
                        Text(label, Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
} 