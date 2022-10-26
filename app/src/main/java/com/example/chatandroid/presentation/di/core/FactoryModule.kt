package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.domain.usecases.chat.ListChatUserCase
import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendImageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.login.RegisterUseCase
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase
import com.example.chatandroid.domain.usecases.users.UpdateUserUseCase
import com.example.chatandroid.presentation.chat.chatList.ChatsViewModelFactory
import com.example.chatandroid.presentation.chat.messages.ChatActivityViewModelFactory
import com.example.chatandroid.presentation.login.LoginViewModelFactory
import com.example.chatandroid.presentation.login.SignUpViewModelFactory
import com.example.chatandroid.presentation.profile.ProfileViewModelFactory
import com.example.chatandroid.presentation.splash.SplashViewModelFactory
import com.example.chatandroid.presentation.users.MainActivityViewModelFactory
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
    fun profileViewModelFactory(
        updateUserUseCase: UpdateUserUseCase,
        getCurrentUserUseCase: GetCurrentUserUseCase
    ): ProfileViewModelFactory {
        return ProfileViewModelFactory(updateUserUseCase,getCurrentUserUseCase)
    }

    @Singleton
    @Provides
    fun loginViewModelFactory(
        loginUsercase: LoginUsercase,
        currentUserUseCase: GetCurrentUserUseCase
    ): LoginViewModelFactory {
        return LoginViewModelFactory(loginUsercase)
    }

    @Singleton
    @Provides
    fun splashViewModelFactory(
        currentUserUseCase: GetCurrentUserUseCase
    ): SplashViewModelFactory {
        return SplashViewModelFactory(currentUserUseCase)
    }

    @Singleton
    @Provides
    fun mainActivityViewModelFactory(
        getUsersUseCase: GetUsersUseCase
    ): MainActivityViewModelFactory {
        return MainActivityViewModelFactory(getUsersUseCase)
    }


    @Singleton
    @Provides
    fun chatActivityViewModelFactory(
        listMessageUseCase: ListMessageUseCase,
        sendMessageUseCase: SendMessageUseCase,
        sendImageUseCase: SendImageUseCase
    ): ChatActivityViewModelFactory {
        return ChatActivityViewModelFactory(listMessageUseCase, sendMessageUseCase, sendImageUseCase)
    }

    @Singleton
    @Provides
    fun chatsListActivityViewModelFactory(
        listChatUserCase: ListChatUserCase,
        logoutUseCase: LogoutUseCase
    ): ChatsViewModelFactory {
        return ChatsViewModelFactory(listChatUserCase,logoutUseCase)
    }



}