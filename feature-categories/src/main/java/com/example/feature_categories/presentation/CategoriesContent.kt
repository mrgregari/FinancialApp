package com.example.feature_categories.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core_data.domain.models.Category
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.components.SearchBar


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesContent(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            SearchBar(
                query = searchText,
                onQueryChanged = onSearchTextChanged
            )
            HorizontalDivider()
        }

        items(
            items = categories,
            key = { it.id }
        ) { category ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = category.icon,
                title = category.name
            )
            HorizontalDivider()
        }
    }
}