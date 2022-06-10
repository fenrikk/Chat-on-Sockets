package com.nikfen.testtask9.viewModel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.nikfen.testtask9.other.USERNAME_SHARED_PREFERENCES

class AuthorizationViewModel(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    fun saveUsername(name: String) {
        sharedPreferences.edit {
            this.putString(
                USERNAME_SHARED_PREFERENCES, name
            )
        }
    }

    fun getUsername(): String? =
        sharedPreferences.getString(USERNAME_SHARED_PREFERENCES, "")
}