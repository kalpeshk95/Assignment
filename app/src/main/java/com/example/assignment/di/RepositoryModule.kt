package com.example.assignment.di

import com.example.assignment.data.repository.NetworkRepository
import org.koin.dsl.module

val repoModule = module {
    single { NetworkRepository(get(), get()) }
}