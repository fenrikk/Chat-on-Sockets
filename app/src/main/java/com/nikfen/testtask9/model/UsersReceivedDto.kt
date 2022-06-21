package com.nikfen.testtask9.model

data class UsersReceivedDto(val users: List<User>) : Payload

data class User(val id: String, val name: String)