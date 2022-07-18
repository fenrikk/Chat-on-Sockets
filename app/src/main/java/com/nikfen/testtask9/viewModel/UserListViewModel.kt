package com.nikfen.testtask9.viewModel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikfen.testtask9.model.User
import com.nikfen.testtask9.model.repository.MainRepository
import com.nikfen.testtask9.other.USERNAME_SHARED_PREFERENCES
import io.reactivex.rxjava3.schedulers.Schedulers

class UserListViewModel(
    private val sharedPreferences: SharedPreferences,
    private val mainRepository: MainRepository
) : ViewModel() {
    private val users: MutableLiveData<List<User>> = MutableLiveData<List<User>>()

    init {
        sharedPreferences.getString(USERNAME_SHARED_PREFERENCES, "")?.let {
            mainRepository.connect(
                it
            )
        }
        mainRepository.getConnection()
            .subscribe({
                mainRepository.startReceivingUsers()
            }, {
                it.printStackTrace()
            })
        mainRepository.usersReceived()
            .subscribeOn(Schedulers.io())
            .subscribe({
                users.postValue(it.users)
            }, {
                it.printStackTrace()
            })
    }

    fun getUsers(): LiveData<List<User>> = users

    fun resetUser() {
        sharedPreferences.edit {
            putString(USERNAME_SHARED_PREFERENCES, "")
        }
    }

    fun stop() {
        mainRepository.stop()
    }
}