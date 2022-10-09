package com.example.chatandroid.presentation.chat.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase

class ChatActivityViewModelFactory(private val listMessageUseCase: ListMessageUseCase, private val sendMessageUseCase: SendMessageUseCase): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatActivityViewModel(listMessageUseCase, sendMessageUseCase) as T
    }
}