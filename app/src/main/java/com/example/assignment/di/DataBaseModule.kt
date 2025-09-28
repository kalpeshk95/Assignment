package com.example.assignment.di

import android.content.Context
import androidx.room.Room
import com.example.assignment.data.local.LocalDataSource
import com.example.assignment.data.local.LocalDataSourceImpl
import com.example.assignment.data.local.db.AppDatabase
import com.example.assignment.data.local.db.UserHoldingDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        getLocalDatabase(androidContext = androidContext())
    }

    single { getLocalDao(get()) }

    // Data sources
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
}

private fun getLocalDao(localDatabase: AppDatabase): UserHoldingDao = localDatabase.userHoldingDao()

private fun getLocalDatabase(androidContext: Context): AppDatabase {
    return Room.databaseBuilder(
        androidContext,
        AppDatabase::class.java,
        "app_database.db"
    ).build()
}