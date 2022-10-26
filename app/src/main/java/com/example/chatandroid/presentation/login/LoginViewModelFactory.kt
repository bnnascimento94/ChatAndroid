package com.example.chatandroid.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase

class LoginViewModelFactory(private val loginUsercase: LoginUsercase)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginUsercase) as T
    }
}