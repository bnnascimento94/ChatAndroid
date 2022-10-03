package com.example.chatandroid.domain.usecases.login

import android.graphics.Bitmap
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository
import java.io.File

class RegisterUseCase(private val loginRepository: LoginRepository,
                      private val databaseRepository: DatabaseRepository) {

    suspend fun execute(userName:String,email:String,password:String, photo: Bitmap): Resource<Boolean>? {

        return try {
            when(loginRepository.registerUser(userName,email,password)){
                is Resource.Loading ->{
                    Resource.Loading(true)
                }
                is Resource.Error ->{
                    Resource.Error("Não foi possível registrar o usuário.",null)
                }
                is Resource.Success ->{
                    databaseRepository.insertUser(userName,email,password, photo)
                }
                else -> {
                    Resource.Error("Não foi possível registrar o usuário.",null)
                }
            }
        }catch (e: Exception){
            Resource.Error("Não foi possível registrar o usuário.",null)
        }


    }


}