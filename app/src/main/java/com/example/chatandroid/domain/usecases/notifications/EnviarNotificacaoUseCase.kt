package com.example.chatandroid.domain.usecases.notifications


import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.NotificacaoRepository

class EnviarNotificacaoUseCase(private val notificacaoRepository: NotificacaoRepository) {

    suspend fun execute(tokenEnvio: String, nomeDestinatario: String)
    : Resource<Boolean> = notificacaoRepository.enviarNotificacao(tokenEnvio,nomeDestinatario)
}