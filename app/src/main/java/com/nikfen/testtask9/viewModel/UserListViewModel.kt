package com.nikfen.testtask9.viewModel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.nikfen.testtask9.other.USERNAME_SHARED_PREFERENCES

class UserListViewModel(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    fun resetUser() {
        sharedPreferences.edit {
            putString(USERNAME_SHARED_PREFERENCES, "")
        }
    }
}