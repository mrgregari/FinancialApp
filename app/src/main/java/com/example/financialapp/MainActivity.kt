package com.example.financialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.financialapp.navigation.AppNavGraph
import com.example.financialapp.ui.theme.FinancialAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialAppTheme(dynamicColor = false) {
                AppNavGraph()
            }

        }
    }
}

@Preview
@Composable
fun PreviewAppNavGraph() {
    FinancialAppTheme(dynamicColor = false) {
        AppNavGraph()
    }
}
