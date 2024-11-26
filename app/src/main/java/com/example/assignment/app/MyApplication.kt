package com.example.assignment.app

import android.app.Application
import com.example.assignment.BuildConfig
import com.example.assignment.di.appModule
import com.example.assignment.di.repoModule
import com.example.assignment.di.retrofitModule
import com.example.assignment.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI() {
        startKoin {

            if (BuildConfig.DEBUG) {
                androidLogger(Level.ERROR)
            }

            androidContext(this@MyApplication)

            modules(
                listOf(
                    appModule,
                    repoModule,
                    retrofitModule,
                    viewModelModule
                )
            )
        }
    }
}