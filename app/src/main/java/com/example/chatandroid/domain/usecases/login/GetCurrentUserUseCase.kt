package com.example.chatandroid.domain.usecases.login

import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.LoginRepository

class GetCurrentUserUseCase(private val loginRepository: LoginRepository) {
    suspend fun execute(): Resource<User>? = loginRepository.getUserConnected()

}