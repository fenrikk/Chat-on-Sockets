package com.nikfen.testtask9.model

import com.nikfen.testtask9.model.Payload

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload