package com.example.chatandroid.data.repository.login

import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val firebaseAuth: FirebaseAuth) : LoginRepository {
    override suspend fun logoutUser(): Resource<Boolean>? {
        return try {
            val currentUser = firebaseAuth.currentUser
            if(currentUser != null){
                firebaseAuth.signOut()
                Resource.Success(true)
            }else{
                Resource.Error("")
            }
        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun getUserConnected(): Resource<User>? {
        return try {
            val currentUser = firebaseAuth.currentUser
            if(currentUser != null){
                Resource.Success(User(currentUser.displayName,currentUser.email,currentUser.uid, null))
            }else{
                Resource.Error("")
            }
        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun getLogin(username: String, password: String): Resource<Boolean>? {
        return try{
            Resource.Loading(true)
            val data = firebaseAuth.signInWithEmailAndPassword(username,password).await()
            val uidUser: String? =  data.user?.uid;

            if(uidUser != null){
                Resource.Loading(false)
                Resource.Success(true)
            }else{
                Resource.Loading(false)
                Resource.Error("Couldn't login..")
            }
        }catch (e : Exception){
            Resource.Loading(false)
            return Resource.Error(e.message)
        }
    }

    override suspend fun registerUser(
        name: String,
        username: String,
        password: String
    ): Resource<Boolean>? {
        return try{
            val data = firebaseAuth.createUserWithEmailAndPassword(username!!,password!!).await()
            val uidUser: String? =  data.user?.uid;

            if(uidUser != null){
                Resource.Success(true)
            }else{
                Resource.Error("Couldn't register user..")
            }
        }catch (e : Exception){
            return Resource.Error(e.message)
        }
    }

    override suspend fun forgotPassword(email: String): Resource<Boolean>? {
        return try{
            val data = firebaseAuth.sendPasswordResetEmail(email).isSuccessful
            return Resource.Success(data)
        }catch (e : Exception){
            return Resource.Error(e.message)
        }
    }
}