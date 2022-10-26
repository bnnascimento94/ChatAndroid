package com.example.chatandroid.presentation.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import com.example.chatandroid.domain.usecases.users.UpdateUserUseCase
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val updateUserUseCase: UpdateUserUseCase,
                       private val getCurrentUserUseCase: GetCurrentUserUseCase)
    : ViewModel() {
    private val _updateUser: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updateUser: LiveData<Resource<Boolean>> = _updateUser
    var filePhoto: File? = null

    private val _getUser: MutableLiveData<Resource<User>> = MutableLiveData()
    val getUser: LiveData<Resource<User>> = _getUser

    fun updateUser(nome:String, photo: Bitmap) = viewModelScope.launch {
        _updateUser.postValue(Resource.Loading())
        _updateUser.postValue(updateUserUseCase.execute(nome,photo))
    }

    fun getUser() = viewModelScope.launch {
        _getUser.postValue(Resource.Loading())
        _getUser.postValue(getCurrentUserUseCase.execute())
    }

    fun rotacionarImagem() : Bitmap {
        return ImageSaver.rotateImage(filePhoto!!.absolutePath)
    }


}