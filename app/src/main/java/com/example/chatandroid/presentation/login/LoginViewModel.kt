package com.example.chatandroid.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUsercase: LoginUsercase) : ViewModel(){

    private val _loginUser: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val loginUser: LiveData<Resource<Boolean>> = _loginUser

    fun logarUsuario(email:String, password: String) = viewModelScope.launch {
        _loginUser.postValue(Resource.Loading())
        _loginUser.postValue(loginUsercase.execute(email,password))
    }


}