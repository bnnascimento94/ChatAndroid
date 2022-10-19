package com.example.chatandroid.presentation.chat.messages

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendImageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class ChatActivityViewModel(private val listMessageUseCase: ListMessageUseCase,
                            private val sendMessageUseCase: SendMessageUseCase,
                            private val sendImageUseCase: SendImageUseCase
                            ): ViewModel() {
    private val _sendMessage: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val sendMessage: LiveData<Resource<Boolean>> = _sendMessage
    var filePhoto: File? = null

    fun getMessages(receiverUid: String) = liveData<Resource<List<Message>>>(Dispatchers.IO) {
        emit(Resource.Loading())
        try{
            listMessageUseCase.execute(receiverUid)?.collect {
                emit(it)
            }

        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }

    fun sendMessages(receiverUid: String, message:String) = viewModelScope.launch {
        _sendMessage.postValue(sendMessageUseCase.execute(receiverUid, message))
    }

    fun sendPhotoMessage(receiverUid: String,bitmap: Bitmap) = viewModelScope.launch {
        sendImageUseCase.execute(receiverUid, bitmap)
    }


    fun rotacionarImagem() : Bitmap {
        return ImageSaver.rotateImage(filePhoto!!.absolutePath)
    }

}