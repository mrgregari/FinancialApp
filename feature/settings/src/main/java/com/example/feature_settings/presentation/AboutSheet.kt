package com.example.feature_settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.example.core_ui.R
import java.text.SimpleDateFormat
import java.util.Date
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSheet(
    showAboutSheet: Boolean,
    onDismiss: () -> Unit,
    context: Context
) {
    if (showAboutSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                var versionName: String
                var versionCode: Long
                var lastUpdate: String
                try {
                    val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                    versionName = pInfo.versionName ?: "-"
                    versionCode = pInfo.longVersionCode
                    val lastUpdateTime = pInfo.lastUpdateTime
                    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
                    lastUpdate = sdf.format(Date(lastUpdateTime))
                } catch (e: Exception) {
                    versionName = "-"
                    versionCode = -1
                    lastUpdate = "-"
                }
                Text(
                    text = stringResource(
                        R.string.version,
                        versionName,
                        versionCode
                    )
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(
                        R.string.update,
                        lastUpdate
                    )
                )
            }
        }
    }
} 