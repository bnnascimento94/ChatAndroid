package com.example.chatandroid.domain.usecases.chat

import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

class ListMessageUseCase(private val databaseRepository: DatabaseRepository) {
    suspend fun execute(receiverUid:String): Flow<Resource<List<Message>>>? {
         return databaseRepository.listMessages(receiverUid)?.map { it -> setMessagePhoto(it) }
    }

    private suspend fun setMessagePhoto( r : Resource<List<Message>>): Resource<List<Message>>{
        return try {
            when(r){
                is Resource.Loading ->{Resource.Loading()}
                is Resource.Error -> {Resource.Error(r.message)}
                is Resource.Success -> {
                    r.data?.map { message ->

                        if (message.photoMessage != null){
                            message.photoMessage = databaseRepository.searchFotoUrl(message.photoMessage!!)
                        }
                    }
                    Resource.Success(r.data!!)

                }
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }
}