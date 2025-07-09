package com.example.feature_expenses.di

import androidx.lifecycle.ViewModel
import com.example.core_ui.di.ViewModelKey
import com.example.financialapp.ui.screens.expenses.ExpensesViewModel
import com.example.financialapp.ui.screens.expensesHistory.ExpensesHistoryViewModel
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
} 