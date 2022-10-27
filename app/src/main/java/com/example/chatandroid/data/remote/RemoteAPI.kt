package com.example.chatandroid.data.remote

import com.example.chatandroid.data.remote.dto.NotificationMessage
import com.example.chatandroid.data.remote.dto.RespostaNotification
import retrofit2.Response
import retrofit2.http.*

interface RemoteAPI {

    companion object{
        const val BASE_URL ="https://fcm.googleapis.com/"
    }

    @Headers("Content-Type: application/json")
    @POST("fcm/send")
    suspend fun sendMessage(
        @Header("Authorization") autorization:String,
        @Body notificationMessage: NotificationMessage?
    ): Response<RespostaNotification>

}