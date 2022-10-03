package com.example.chatandroid.domain.usecases.users

import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository

class GetUsersUseCase(private val databaseRepository: DatabaseRepository)  {
    suspend fun execute(): Resource<List<User>>? = databaseRepository.getListOfUsers()
}