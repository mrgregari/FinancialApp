package com.example.feature_incomes.di

import androidx.lifecycle.ViewModel
import com.example.core_ui.di.ViewModelKey
import com.example.feature_incomes.presentation.addIncome.AddIncomeViewModel
import com.example.feature_incomes.presentation.todayIncomes.IncomesViewModel
import com.example.feature_incomes.presentation.incomesHistory.IncomesHistoryViewModel
import com.example.feature_incomes.presentation.editIncome.EditIncomeViewModel
import com.example.feature_incomes.presentation.analytics.IncomeAnalyticsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface IncomesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    fun bindIncomesViewModel(viewModel: IncomesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomesHistoryViewModel::class)
    fun bindIncomesHistoryViewModel(viewModel: IncomesHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddIncomeViewModel::class)
    fun bindAddIncomeViewModel(viewModel: AddIncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditIncomeViewModel::class)
    fun bindEditIncomeViewModel(viewModel: EditIncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeAnalyticsViewModel::class)
    fun bindIncomeAnalyticsViewModel(viewModel: IncomeAnalyticsViewModel): ViewModel
} 