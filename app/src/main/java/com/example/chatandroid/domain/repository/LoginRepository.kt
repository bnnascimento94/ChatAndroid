package com.example.chatandroid.domain.repository

import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource

interface LoginRepository {

    suspend fun logoutUser(): Resource<Boolean>?

    suspend fun getUserConnected(): Resource<User>?

    suspend fun getLogin(username:String,password:String): Resource<Boolean>?

    suspend fun registerUser(name:String,username:String,password:String): Resource<Boolean>?

    suspend fun forgotPassword(email:String): Resource<Boolean>?

}