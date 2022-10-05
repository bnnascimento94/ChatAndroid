package com.example.chatandroid.domain.usecases.users

import android.graphics.Bitmap
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository

class UpdateUserUseCase(private val databaseRepository: DatabaseRepository)  {
    suspend fun execute(name:String, photo:Bitmap): Resource<Boolean>? = databaseRepository.updateUser(name,photo)
}