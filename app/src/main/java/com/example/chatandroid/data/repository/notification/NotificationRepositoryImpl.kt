package com.example.chatandroid.data.repository.notification


import com.example.chatandroid.data.remote.RemoteAPI
import com.example.chatandroid.data.remote.dto.Data
import com.example.chatandroid.data.remote.dto.NotificationMessage
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.NotificacaoRepository

class NotificationRepositoryImpl(private val remoteAPI: RemoteAPI) : NotificacaoRepository{

    override suspend fun enviarNotificacao(token: String, nomeCliente: String): Resource<Boolean> {
        return try {
            val authorization = "key=AAAAnwbm4L8:APA91bHxybt7DY1BYvQ_Bh-cRirHfRS0Rfzj1KiT_YED6o9nWuLAZDAa32OdN6G6EKNfKtisPjIY2z4qqxUGDk8UPLJOozKtDRDyWziwfQqoxN3Tss0xCPW2FDES3mDOzOXaoLWAQxGA"
            val data = Data(title = "Nova Mensagem", body = "Nova Mensagem de $nomeCliente")
            val notificationMessage = NotificationMessage(to= token, data = data)
            val response = remoteAPI.sendMessage(authorization,notificationMessage)
            if(response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!.success!! > 0)
            }else{
                Resource.Success(false)
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }
}