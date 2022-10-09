package com.example.chatandroid.domain.repository

import android.graphics.Bitmap
import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DatabaseRepository {

    suspend fun getUserConected(): Resource<User>?

    suspend fun getListOfUsers(): Resource<List<User>>?

    suspend fun insertUser(name:String,username:String,password:String, photo: Bitmap): Resource<Boolean>?

    suspend fun updateUser(name:String, photo: Bitmap): Resource<Boolean>?

    suspend fun insertMessage(receiverUid:String,message:String): Resource<Boolean>?

    suspend fun listChats(): Resource<List<Chat>>?

    suspend fun listMessages(receiverUid:String): Flow<Resource<List<Message>>>?
}