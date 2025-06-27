package com.example.financialapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financialapp.di.ViewModelFactory
import com.example.financialapp.di.ViewModelKey
import com.example.financialapp.ui.screens.account.AccountViewModel
import com.example.financialapp.ui.screens.categories.CategoriesViewModel
import com.example.financialapp.ui.screens.expenses.ExpensesViewModel
import com.example.financialapp.ui.screens.expensesHistory.ExpensesHistoryViewModel
import com.example.financialapp.ui.screens.incomes.IncomesViewModel
import com.example.financialapp.ui.screens.incomesHistory.IncomesHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(viewModel: AccountViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindCategoriesViewModel(viewModel: CategoriesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindExpensesViewModel(viewModel: ExpensesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    fun bindIncomesViewModel(viewModel: IncomesViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    fun bindExpensesHistoryViewModel(viewModel: ExpensesHistoryViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomesHistoryViewModel::class)
    fun bindIncomesHistoryViewModel(viewModel: IncomesHistoryViewModel) : ViewModel

}