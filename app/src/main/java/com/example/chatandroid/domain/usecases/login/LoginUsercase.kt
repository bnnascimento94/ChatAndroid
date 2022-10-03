package com.example.chatandroid.domain.usecases.login

import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.LoginRepository

class LoginUsercase(private val loginRepository: LoginRepository) {
    suspend fun execute(userName:String,password:String): Resource<Boolean>? = loginRepository.getLogin(userName,password)
}