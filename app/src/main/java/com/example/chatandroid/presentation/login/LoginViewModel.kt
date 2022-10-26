package com.example.chatandroid.presentation.login

import androidx.lifecycle.*
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUsercase: LoginUsercase)
    : ViewModel(){

    private val _loginUser: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val loginUser: LiveData<Resource<Boolean>> = _loginUser

    fun logarUsuario(email:String, password: String) = viewModelScope.launch {
        _loginUser.postValue(Resource.Loading())
        _loginUser.postValue(loginUsercase.execute(email,password))
    }



}