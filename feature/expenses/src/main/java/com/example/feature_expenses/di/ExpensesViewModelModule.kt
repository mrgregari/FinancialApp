package com.example.feature_expenses.di

import androidx.lifecycle.ViewModel
import com.example.core_ui.di.ViewModelKey
import com.example.feature_expenses.presentation.addExpense.AddExpenseViewModel
import com.example.feature_expenses.presentation.analytics.ExpenseAnalyticsViewModel
import com.example.feature_expenses.presentation.editExpense.EditExpenseViewModel
import com.example.feature_expenses.presentation.todayExpenses.ExpensesViewModel
import com.example.feature_expenses.presentation.expensesHistory.ExpensesHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExpensesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindExpensesViewModel(viewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    fun bindExpensesHistoryViewModel(viewModel: ExpensesHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddExpenseViewModel::class)
    fun bindAddExpenseViewModel(viewModel: AddExpenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditExpenseViewModel::class)
    fun bindEditExpenseViewModel(viewModel: EditExpenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseAnalyticsViewModel::class)
    fun bindExpenseAnalyticsViewModel(viewModel: ExpenseAnalyticsViewModel): ViewModel
} 