package com.example.chatandroid.presentation.di.core

import android.content.Context
import com.example.chatandroid.presentation.chat.MessageAdapter
import com.example.chatandroid.presentation.users.UserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun userAdapter(@ApplicationContext appContext: Context): UserAdapter {
        return UserAdapter(appContext)
    }

    @Singleton
    @Provides
    fun messageAdapter(@ApplicationContext appContext: Context): MessageAdapter {
        return MessageAdapter(appContext)
    }



}