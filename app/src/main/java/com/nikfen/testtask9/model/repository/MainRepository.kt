package com.nikfen.testtask9.model.repository

interface MainRepository {
    fun getUsers()
    fun sendMassage()
    fun connect(username: String)
}