package com.example.chatandroid.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.login.LoginUsercase

class LoginViewModelFactory(private val loginUsercase: LoginUsercase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginUsercase) as T
    }
}