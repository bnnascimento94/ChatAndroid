package com.example.chatandroid.domain.usecases.chat

import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListMessageUseCase(private val databaseRepository: DatabaseRepository) {
    suspend fun execute(receiverUid:String): Flow<Resource<List<Message>>>? {
         return databaseRepository.listMessages(receiverUid)?.map { it -> setMessagePhoto(it) }
    }

    private suspend fun setMessagePhoto( r : Resource<List<Message>>): Resource<List<Message>>{
        return try {
            when(r){
                is Resource.Loading ->{Resource.Loading()}
                is Resource.Error -> {Resource.Error(r.message)}
                is Resource.Success -> {
                    val mensagensAgrupadas: ArrayList<Message> = ArrayList()

                    r.data?.forEachIndexed { index, message ->
                        if (index > 0){
                           val mensagemAnterior = r.data[index -1]
                            message.time?.let {
                                mensagemAnterior.time?.let {
                                    val dataFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                                    val dataMensagemAnterior = dataFormat.format(Date(it))
                                    val dataMensagemAtual = dataFormat.format(Date(message.time!!))

                                    if(dataFormat.parse(dataMensagemAnterior)!!.before(dataFormat.parse(dataMensagemAtual))){
                                        mensagensAgrupadas.add(Message(messageTitledDate = dataMensagemAtual,null,null,null,null,null ))
                                    }
                                }
                            }
                        }
                        if(message.time != null){
                            val d = Date(message.time!!)
                            val horaFormat: DateFormat = SimpleDateFormat("HH:mm")
                            message.dataTexto = horaFormat.format(d)
                        }
                        if (message.photoMessage != null){
                            message.photoMessage = databaseRepository.searchFotoUrl(message.photoMessage!!)
                        }

                        mensagensAgrupadas.add(message)

                    }

                    Resource.Success(mensagensAgrupadas)

                }
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }
}