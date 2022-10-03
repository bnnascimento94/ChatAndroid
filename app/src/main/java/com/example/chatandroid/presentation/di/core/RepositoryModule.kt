package com.example.chatandroid.presentation.di.core

import com.example.chatandroid.data.repository.database.DatabaseRespositoryImpl
import com.example.chatandroid.data.repository.login.LoginRepositoryImpl
import com.example.chatandroid.domain.repository.DatabaseRepository
import com.example.chatandroid.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(firebaseAuth: FirebaseAuth): LoginRepository {
        return LoginRepositoryImpl(firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabaseRepository(mDbRef: DatabaseReference, firebaseAuth: FirebaseAuth, firebaseStorage: FirebaseStorage): DatabaseRepository {
        return DatabaseRespositoryImpl(mDbRef,firebaseAuth,firebaseStorage)
    }


}