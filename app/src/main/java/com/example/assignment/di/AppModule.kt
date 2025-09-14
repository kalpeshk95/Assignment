package com.example.assignment.di

import com.example.assignment.utils.NetworkStatusHelper
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { NetworkStatusHelper(androidContext()) }
    single { Dispatchers.IO }
}