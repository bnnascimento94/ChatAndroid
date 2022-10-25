package com.example.chatandroid.presentation.chat.chatList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.chat.ListChatUserCase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import kotlinx.coroutines.launch

class ChatsViewModel(private val listChatUserCase: ListChatUserCase, private val logoutUseCase: LogoutUseCase): ViewModel() {

    private val _chatList: MutableLiveData<Resource<List<Chat>>> = MutableLiveData()
    val chatList: LiveData<Resource<List<Chat>>> = _chatList

    private val _logoutUser: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val logoutUser: LiveData<Resource<Boolean>> = _logoutUser

    fun getChatsList() = viewModelScope.launch{
        _chatList.postValue(Resource.Loading())
        _chatList.postValue(listChatUserCase.execute())
    }


    fun logoutUsuario() = viewModelScope.launch {
        _logoutUser.postValue(logoutUseCase.execute())
    }



}