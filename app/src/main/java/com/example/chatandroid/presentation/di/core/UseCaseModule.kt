package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository
import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase
import com.example.chatandroid.domain.usecases.login.GetCurrentUserUseCase
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.login.RegisterUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideRegisterUseCase(loginRepository: LoginRepository, databaseRepository: DatabaseRepository): RegisterUseCase {
        return RegisterUseCase(loginRepository,databaseRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrentUserUseCase(loginRepository: LoginRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(loginRepository)
    }


    @Singleton
    @Provides
    fun provideLoginUserUseCase(loginRepository: LoginRepository): LoginUsercase {
        return LoginUsercase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideLogoutUserUseCase(loginRepository: LoginRepository): LogoutUseCase {
        return LogoutUseCase(loginRepository)
    }

    @Singleton
    @Provides
    fun provideGetUsersUseCase(databaseRepository: DatabaseRepository): GetUsersUseCase {
        return GetUsersUseCase(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideListMessageUseCase(databaseRepository: DatabaseRepository): ListMessageUseCase {
        return ListMessageUseCase(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideSendMessageUseCase(databaseRepository: DatabaseRepository): SendMessageUseCase {
        return SendMessageUseCase(databaseRepository)
    }

}