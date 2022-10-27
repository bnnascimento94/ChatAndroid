package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository
import com.example.chatandroid.domain.repository.NotificacaoRepository
import com.example.chatandroid.domain.usecases.chat.ListChatUserCase
import com.example.chatandroid.domain.usecases.chat.ListMessageUseCase
import com.example.chatandroid.domain.usecases.chat.SendImageUseCase
import com.example.chatandroid.domain.usecases.chat.SendMessageUseCase
import com.example.chatandroid.domain.usecases.users.GetCurrentUserUseCase
import com.example.chatandroid.domain.usecases.login.LoginUsercase
import com.example.chatandroid.domain.usecases.login.LogoutUseCase
import com.example.chatandroid.domain.usecases.login.RegisterUseCase
import com.example.chatandroid.domain.usecases.notifications.EnviarNotificacaoUseCase
import com.example.chatandroid.domain.usecases.users.GetUsersUseCase
import com.example.chatandroid.domain.usecases.users.UpdateUserUseCase
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
    fun provideEnviarNotificationUseCase(notificacaoRepository: NotificacaoRepository): EnviarNotificacaoUseCase {
        return EnviarNotificacaoUseCase(notificacaoRepository)
    }

    @Singleton
    @Provides
    fun provideRegisterUseCase(loginRepository: LoginRepository, databaseRepository: DatabaseRepository): RegisterUseCase {
        return RegisterUseCase(loginRepository,databaseRepository)
    }

    @Singleton
    @Provides
    fun provideGetCurrentUserUseCase(databaseRepository: DatabaseRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateUserUseCase(databaseRepository: DatabaseRepository): UpdateUserUseCase {
        return UpdateUserUseCase(databaseRepository)
    }


    @Singleton
    @Provides
    fun provideLoginUserUseCase(loginRepository: LoginRepository, databaseRepository: DatabaseRepository): LoginUsercase {
        return LoginUsercase(loginRepository, databaseRepository)
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
    fun provideListChatUseCase(databaseRepository: DatabaseRepository): ListChatUserCase {
        return ListChatUserCase(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideSendMessageUseCase(databaseRepository: DatabaseRepository): SendMessageUseCase {
        return SendMessageUseCase(databaseRepository)
    }

    @Singleton
    @Provides
    fun provideSendImageUseCase(databaseRepository: DatabaseRepository): SendImageUseCase {
        return SendImageUseCase(databaseRepository)
    }

}