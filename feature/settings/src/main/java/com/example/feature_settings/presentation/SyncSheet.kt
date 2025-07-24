package com.example.feature_settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.SharedPreferences
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.res.stringResource
import com.example.core_ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncSheet(
    showSyncSheet: Boolean,
    onDismiss: () -> Unit,
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    prefs: SharedPreferences
) {
    if (showSyncSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    stringResource(
                        R.string.sync_interval,
                        sliderValue.toInt()
                    )
                )
                Slider(
                    value = sliderValue,
                    onValueChange = onSliderValueChange,
                    valueRange = 5f..120f,
                    steps = 23
                )
                Button(
                    onClick = {
                        prefs.edit().putInt("sync_interval_minutes", sliderValue.toInt())
                            .apply()
                        onDismiss()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        stringResource(
                            R.string.save
                        )
                    )
                }
            }
        }
    }
} 