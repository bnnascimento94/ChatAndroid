package com.example.chatandroid.domain.usecases.login

import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.LoginRepository

class LogoutUseCase(private val loginRepository: LoginRepository)  {
    suspend fun execute(): Resource<Boolean>? = loginRepository.logoutUser()
}