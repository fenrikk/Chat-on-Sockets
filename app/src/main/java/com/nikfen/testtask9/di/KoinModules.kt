package com.nikfen.testtask9.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nikfen.testtask9.model.repository.MainRepository
import com.nikfen.testtask9.model.repository.MainRepositoryImpl
import com.nikfen.testtask9.other.APPLICATION_SHARED_PREFERENCES
import com.nikfen.testtask9.viewModel.AuthorizationViewModel
import com.nikfen.testtask9.viewModel.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferences> {
        val app: Application = get()
        app.getSharedPreferences(
            APPLICATION_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    single<MainRepository> {
        MainRepositoryImpl()
    }
}
val viewModelModule = module {
    viewModel { AuthorizationViewModel(get()) }
    viewModel { UserListViewModel(get(),get()) }
}