package com.example.assignment.di

import com.example.assignment.ui.portfolio.PortfolioVm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PortfolioVm(get()) }
}
