package com.example.feature_incomes.presentation.editIncome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core_domain.models.Category
import com.example.core_ui.R
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
fun EditIncomeContent(
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
    validationState: TransactionValidationState = TransactionValidationState(),
    onDeleteClick: () -> Unit
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
        Column(modifier = Modifier.fillMaxSize()) {
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = stringResource(R.string.account),
                trailingText = account,
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = stringResource(R.string.category),
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
                label = stringResource(R.string.amount),
                value = value,
                onValueChange = onValueChange,
                keyboardType = KeyboardType.Number,
                hint = stringResource(R.string.enter_sum)
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
                title = stringResource(R.string.date),
                trailingText = dateFormat.format(date),
                showArrow = true,
                onClick = { showDatePicker = true }
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(71.dp),
                title = stringResource(R.string.time),
                trailingText = timeFormat.format(time),
                showArrow = true,
                onClick = { showTimePicker = true }
            )
            HorizontalDivider()
            HintEditField(
                value = comment,
                onValueChange = onCommentChange,
                hint = stringResource(R.string.comment)
            )
            HorizontalDivider()
            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_income),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
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