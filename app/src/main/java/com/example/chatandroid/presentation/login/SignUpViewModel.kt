package com.example.chatandroid.presentation.login

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.usecases.login.RegisterUseCase
import kotlinx.coroutines.launch
import java.io.File

class SignUpViewModel(private val registerUsercase: RegisterUseCase) : ViewModel() {

    private val _registerUser: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val registerUser: LiveData<Resource<Boolean>> = _registerUser

    var filePhoto: File? = null

    fun registerUser(name:String,email:String, password: String, foto:Bitmap) = viewModelScope.launch {
        Resource.Loading(true)
        _registerUser.postValue(registerUsercase.execute(name,email,password, foto))
    }

    fun rotacionarImagem() : Bitmap {
        return ImageSaver.rotateImage(filePhoto!!.absolutePath)
    }

}