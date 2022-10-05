package com.example.chatandroid.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import com.example.chatandroid.domain.usecases.users.UpdateUserUseCase

class ProfileViewModelFactory(private val updateUserUseCase: UpdateUserUseCase,
                              private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(updateUserUseCase, getCurrentUserUseCase) as T
    }

}