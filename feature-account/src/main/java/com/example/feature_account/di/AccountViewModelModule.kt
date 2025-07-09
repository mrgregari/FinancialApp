package com.example.feature_account.di

import androidx.lifecycle.ViewModel
import com.example.core_ui.di.ViewModelKey
import com.example.feature_account.presentation.AccountViewModel
import com.example.feature_account.presentation.accountEdit.AccountEditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AccountViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountEditViewModel::class)
    fun bindAccountEditViewModel(viewModel: AccountEditViewModel): ViewModel
} 