package com.nikfen.testtask9.viewModel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.nikfen.testtask9.model.repository.MainRepository
import com.nikfen.testtask9.other.USERNAME_SHARED_PREFERENCES

class UserListViewModel(
    private val sharedPreferences: SharedPreferences,
    private val mainRepository: MainRepository
) : ViewModel() {
    fun resetUser() {
        sharedPreferences.edit {
            putString(USERNAME_SHARED_PREFERENCES, "")
        }
    }

    fun connect() {
        sharedPreferences.getString(USERNAME_SHARED_PREFERENCES, "")?.let {
            mainRepository.connect(
                it
            )
        }
    }
}