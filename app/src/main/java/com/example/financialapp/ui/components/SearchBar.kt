package com.example.financialapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import com.example.financialapp.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = "Найти статью"
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { 
            Text(
                text = placeholder
            ) 
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.padding(19.dp),
                painter = painterResource(R.drawable.search),
                contentDescription = "Search"
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
        )
    )
}