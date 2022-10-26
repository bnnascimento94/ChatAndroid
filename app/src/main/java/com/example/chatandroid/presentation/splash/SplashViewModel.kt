package com.example.chatandroid.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers

class SplashViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    fun userLoaded() = liveData<Resource<User>>(Dispatchers.IO) {
        emit(Resource.Loading())
        emit(getCurrentUserUseCase.execute()!!)
    }

}