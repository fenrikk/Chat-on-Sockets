package com.nikfen.testtask9.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikfen.testtask9.model.repository.MainRepository

class ChatViewModel(
    private val chatWithId: String,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val chat: MutableLiveData<List<String>> = MutableLiveData()
    private val chatHistory: MutableList<String> = mutableListOf()

    init {
        mainRepository.newMessageReceived()
            .filter { it.from.id == chatWithId }
            .subscribe({
                chatHistory += "${it.from.name}: ${it.message}"
                chat.value = chatHistory
            }, {
                it.printStackTrace()
            })
    }

    fun sendMessage(receiver: String, message: String) {
        mainRepository.sendMassage(receiver, message)
        chatHistory += "me: $message"
        chat.value = chatHistory
    }

    fun getMessages(): LiveData<List<String>> = chat

    fun stop() {
        mainRepository.stop()
    }
}