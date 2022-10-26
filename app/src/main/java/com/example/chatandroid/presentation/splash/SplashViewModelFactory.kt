package com.example.chatandroid.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase

class SplashViewModelFactory(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(getCurrentUserUseCase) as T
    }
}