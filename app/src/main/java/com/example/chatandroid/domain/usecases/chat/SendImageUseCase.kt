package com.example.chatandroid.domain.usecases.chat

import android.graphics.Bitmap
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository

class SendImageUseCase(private val databaseRepository: DatabaseRepository) {

    suspend fun execute(receiverUid:String, photo: Bitmap): Resource<Boolean>? = databaseRepository.insertFotoMessage(receiverUid, photo)

}