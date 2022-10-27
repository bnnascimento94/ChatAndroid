package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.data.remote.RemoteAPI
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    fun provideRemoteAPI():RemoteAPI{
        return Retrofit.Builder()
            .baseUrl(RemoteAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteAPI::class.java)
    }

}