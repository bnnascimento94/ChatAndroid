package com.example.chatandroid.domain.usecases.login

import android.content.ContentValues.TAG
import android.util.Log
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class LoginUsercase(private val loginRepository: LoginRepository, private val databaseRepository: DatabaseRepository) {
    suspend fun execute(userName:String,password:String): Resource<Boolean>?{
        return try {
            val result = loginRepository.getLogin(userName,password)
            when(result!!){
                is Resource.Loading ->{
                    Resource.Loading(true)
                }
                is Resource.Error ->{
                    Resource.Error(result.message, null)
                }
                is Resource.Success ->{
                    val token = FirebaseMessaging.getInstance().token.await()
                    databaseRepository.updateToken(token)
                }

            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }

    }
}