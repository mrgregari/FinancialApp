package com.example.feature_expenses.presentation.addExpense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core_domain.models.Category
import com.example.core_ui.components.CancelSheetItem
import com.example.core_ui.components.CustomDatePickerDialog
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.components.CustomTimePickerDialog
import com.example.core_ui.components.EditField
import com.example.core_ui.components.HintEditField
import com.example.core_ui.utils.TransactionValidationState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseContent(
    account: String,
    selectedCategory: String?,
    value: String,
    onValueChange: (String) -> Unit,
    date: Date,
    time: Date,
    comment: String,
    onDateChange: (Date) -> Unit,
    onTimeChange: (Date) -> Unit,
    onCommentChange: (String) -> Unit,
    categories: List<Category> = emptyList(),
    onCategorySelected: (Category) -> Unit,
    validationState: TransactionValidationState = TransactionValidationState()
) {

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column {
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = "Счёт",
                trailingText = account,
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = "Статья",
                showArrow = true,
                trailingText = selectedCategory ?: "",
                onClick = { showCategorySheet = true }
            )
            validationState.categoryError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                )
            }
            HorizontalDivider()
            EditField(
                label = "Сумма",
                value = value,
                onValueChange = onValueChange,
                keyboardType = KeyboardType.Number,
                hint = "Введите сумму"
            )
            validationState.valueError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                )
            }
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = "Дата",
                trailingText = dateFormat.format(date),
                showArrow = true,
                onClick = { showDatePicker = true }
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = "Время",
                trailingText = timeFormat.format(time),
                showArrow = true,
                onClick = { showTimePicker = true }
            )
            HorizontalDivider()
            HintEditField(
                value = comment,
                onValueChange = onCommentChange,
                hint = "Комментарий"
            )
            HorizontalDivider()
        }
        if (showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = { showCategorySheet = false }
            ) {
                LazyColumn {
                    items(categories) { category ->
                        CustomListItem(
                            modifier = Modifier.height(71.dp),
                            emoji = category.icon,
                            title = category.name,
                            onClick = {
                                onCategorySelected(category)
                                showCategorySheet = false
                            }
                        )
                        HorizontalDivider()
                    }
                    item {
                        CancelSheetItem(onClick = { showCategorySheet = false })
                    }
                }
            }
        }
    }

    CustomDatePickerDialog(
        showDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedDate ->
            if (selectedDate != null) {
                val cal = Calendar.getInstance().apply { this.time = selectedDate }
                val current = Calendar.getInstance().apply { this.time = time }
                cal.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY))
                cal.set(Calendar.MINUTE, current.get(Calendar.MINUTE))
                onDateChange(cal.time)
            }
        }
    )

    if (showTimePicker) {
        val cal = Calendar.getInstance().apply { this.time = time }
        CustomTimePickerDialog(
            showDialog = showTimePicker,
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            onDismiss = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                val calDate = Calendar.getInstance().apply { this.time = date }
                calDate.set(Calendar.HOUR_OF_DAY, hour)
                calDate.set(Calendar.MINUTE, minute)
                calDate.set(Calendar.SECOND, 0)
                calDate.set(Calendar.MILLISECOND, 0)
                onTimeChange(calDate.time)
                showTimePicker = false
            }
        )
    }
}