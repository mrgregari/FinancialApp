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
fun LanguageSheet(
    showLanguageSheet: Boolean,
    onDismiss: () -> Unit,
    currentLang: String,
    prefs: SharedPreferences,
    onLangSelected: () -> Unit
) {
    if (showLanguageSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.padding(16.dp)) {
                listOf("ru" to "Русский", "en" to "English").forEach { (code, label) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                prefs.edit().putString("app_lang", code).apply()
                                onDismiss()
                                onLangSelected()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLang == code,
                            onClick = {
                                prefs.edit().putString("app_lang", code).apply()
                                onDismiss()
                                onLangSelected()
                            }
                        )
                        Text(label, Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
} 