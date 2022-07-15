package com.nikfen.testtask9.model.repository

import com.nikfen.testtask9.model.MessageDto
import com.nikfen.testtask9.model.UsersReceivedDto
import io.reactivex.rxjava3.core.Observable

interface MainRepository {
    fun getUsers()
    fun sendMassage()
    fun connect(username: String)
    fun getConnection(): Observable<Boolean>
    fun newMessageReceived(): Observable<MessageDto>
    fun usersReceived(): Observable<UsersReceivedDto>
}