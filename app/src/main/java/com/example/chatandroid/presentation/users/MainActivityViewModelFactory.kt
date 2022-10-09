package com.example.chatandroid.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase

class MainActivityViewModelFactory(private val getUsersUseCase: GetUsersUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(getUsersUseCase) as T
    }
}