package com.example.chatandroid.domain.usecases.chat

import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class ListMessageUseCase(private val databaseRepository: DatabaseRepository) {
    suspend fun execute(receiverUid:String): Flow<Resource<List<Message>>>? = databaseRepository.listMessages(receiverUid)
}