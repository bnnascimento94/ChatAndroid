package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.login.RegisterUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase
import com.example.chatandroid.presentation.chat.ChatActivityViewModelFactory
import com.example.chatandroid.presentation.users.MainActivityViewModelFactory
import com.example.chatandroid.presentation.login.LoginViewModelFactory
import com.example.chatandroid.presentation.login.SignUpViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun signUpViewModelFactory(
        registerUsercase: RegisterUseCase
    ): SignUpViewModelFactory {
        return SignUpViewModelFactory(registerUsercase)
    }

    @Singleton
    @Provides
    fun loginViewModelFactory(
        loginUsercase: LoginUsercase
    ): LoginViewModelFactory {
        return LoginViewModelFactory(loginUsercase)
    }

    @Singleton
    @Provides
    fun mainActivityViewModelFactory(
        getUsersUseCase: GetUsersUseCase,
        logoutUseCase: LogoutUseCase
    ): MainActivityViewModelFactory {
        return MainActivityViewModelFactory(getUsersUseCase,logoutUseCase)
    }


    @Singleton
    @Provides
    fun chatActivityViewModelFactory(
        listMessageUseCase: ListMessageUseCase,
        sendMessageUseCase: SendMessageUseCase
    ): ChatActivityViewModelFactory {
        return ChatActivityViewModelFactory(listMessageUseCase, sendMessageUseCase)
    }


}