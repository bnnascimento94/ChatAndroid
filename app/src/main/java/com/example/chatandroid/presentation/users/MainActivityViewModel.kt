package com.example.chatandroid.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase
import kotlinx.coroutines.launch

class MainActivityViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {


    private val _getUsers: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val getUsers: LiveData<Resource<List<User>>> = _getUsers



    fun getUsers() = viewModelScope.launch {
        _getUsers.postValue(Resource.Loading())
        _getUsers.postValue(getUsersUseCase.execute())
    }



}