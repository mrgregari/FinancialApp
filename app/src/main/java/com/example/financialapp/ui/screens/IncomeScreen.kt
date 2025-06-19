package com.example.financialapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financialapp.R
import com.example.financialapp.domain.model.Expenses
import com.example.financialapp.domain.model.Income
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.CustomFab
import com.example.financialapp.ui.utils.formatNumber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun IncomeScreen() {

    val list = listOf<Income>(
        Income(
            title = "Заплата",
            amount = 100000,
            account = "Сбербанк",
            date = "08.06.2025",
        ),
        Income(
            title = "Подработка",
            amount = 100,
            account = "Сбербанк",
            date = "08.06.2025"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Доходы сегодня") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.history),
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CustomFab(onClick = { })
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            stickyHeader {
                CustomListItem(
                    modifier = Modifier
                        .height(56.dp),
                    title = "Всего:",
                    subTitle = null,
                    trailingText = "${formatNumber(list.sumOf { it.amount })} $",
                    subTrailingText = null,
                    showArrow = false,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            items(list) { expense ->
                CustomListItem(
                    modifier = Modifier
                        .height(70.dp),
                    title = expense.title,
                    subTitle = expense.comment,
                    trailingText = "${formatNumber(expense.amount)} $",
                    subTrailingText = null,
                    showArrow = true,
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }

    }
}