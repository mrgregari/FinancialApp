package com.example.financialapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financialapp.domain.model.Category
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen() {

    val list = listOf(
        Category(
            name = "Аренда квартиры",
            icon = "\uD83C\uDFE0",
            isIncome = false
        ),
        Category(
            name = "На собачку",
            icon = "\uD83D\uDC36",
            isIncome = false
        ),
        Category(
            name = "Ремонт квартиры",
            icon = "РК",
            isIncome = false
        ),
        Category(
            name = "Продукты",
            icon = "\uD83C\uDF6D",
            isIncome = false
        ),
        Category(
            name = "Одежда",
            icon = "\uD83D\uDC57",
            isIncome = false
        ),
        Category(
            name = "Медицина",
            icon = "\uD83D\uDC8A",
            isIncome = false
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои статьи") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            stickyHeader {
                SearchBar()
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }


            items(list) { category ->
                CustomListItem(
                    modifier = Modifier
                        .height(70.dp),
                    emoji = category.icon,
                    title = category.name
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

