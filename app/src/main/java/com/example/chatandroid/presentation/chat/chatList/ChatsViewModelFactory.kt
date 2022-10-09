package com.example.chatandroid.presentation.chat.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.chat.ListChatUserCase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase

class ChatsViewModelFactory(val listChatUserCase: ListChatUserCase, val logoutUseCase: LogoutUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(listChatUserCase,logoutUseCase) as T
    }
}