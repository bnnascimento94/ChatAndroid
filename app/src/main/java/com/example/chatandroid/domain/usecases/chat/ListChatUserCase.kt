package com.example.chatandroid.domain.usecases.chat

import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository

class ListChatUserCase(val databaseRepository: DatabaseRepository) {
    suspend fun execute(): Resource<List<Chat>>? = databaseRepository.listChats()
}