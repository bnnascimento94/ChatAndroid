package com.example.chatandroid.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.login.RegisterUseCase

class SignUpViewModelFactory(private val registerUsercase: RegisterUseCase): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(registerUsercase) as T
    }
}