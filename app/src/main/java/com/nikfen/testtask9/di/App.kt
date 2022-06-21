package com.nikfen.testtask9.di

import android.app.Application
import com.nikfen.testtask9.di.dataModule
import com.nikfen.testtask9.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}