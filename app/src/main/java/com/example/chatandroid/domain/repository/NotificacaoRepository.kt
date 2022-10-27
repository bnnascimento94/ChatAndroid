package com.example.chatandroid.domain.repository

import com.example.chatandroid.data.util.Resource


interface NotificacaoRepository {

    suspend fun enviarNotificacao(token:String, nomeCliente: String) : Resource<Boolean>

}