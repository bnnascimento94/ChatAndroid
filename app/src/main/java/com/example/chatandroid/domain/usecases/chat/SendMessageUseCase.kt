package com.example.chatandroid.domain.usecases.chat

import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository

class SendMessageUseCase(private val databaseRepository: DatabaseRepository){
    suspend fun execute(receiverUid:String, message:String): Resource<Boolean>? = databaseRepository.insertMessage(receiverUid, message)
}