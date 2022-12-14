package com.example.chatandroid.data.repository.database

import android.graphics.Bitmap
import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DatabaseRespositoryImpl(val mDbRef: DatabaseReference, val firebaseAuth: FirebaseAuth,val firebaseStorage: FirebaseStorage): DatabaseRepository {


    override suspend fun getUserConected(): Resource<User>? {
        return try{
            val user = firebaseAuth.currentUser
            if(user != null){
              val user =  mDbRef.child("user").child(user!!.uid).get().await().getValue(User::class.java)
              if(user?.photoName != null){
                  user!!.photoName = searchFotoUrl(user.photoName!!)
              }
              Resource.Success(user!!)
            }else{
                Resource.Error("Usuário Não Encontrado!")
            }

        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }




    private suspend fun deletePhoto(uid: String?){
        try {
            val storageRef = firebaseStorage.getReference()
            val desertRef = storageRef.child("usuario/${uid!!}.jpg")
            desertRef.delete().await()
        }catch (exception: Exception){ }

    }

    override suspend fun getListOfUsers(): Resource<List<User>>? {
        return try{
            val user = firebaseAuth.currentUser
            val usuarios = mDbRef.child("user").get().await()
            if(usuarios.exists()){
                val u = ArrayList<User>()
                for(postSnapshot in usuarios.children){
                    val currentuser = postSnapshot.getValue(User::class.java)
                    if(!currentuser?.uid.equals(user?.uid)){
                        if(currentuser!!.photoName != null){
                            currentuser!!.photoName = searchFotoUrl(currentuser!!.photoName!!)
                        }
                        u.add(currentuser!!)
                    }
                }
                Resource.Success(u)
            }else{
                Resource.Error("")
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun insertUser(
        name: String,
        username: String,
        password: String,
        photo: Bitmap
    ): Resource<Boolean>? {
        return try {
            val user = firebaseAuth.currentUser
            if(user != null){
                val photoName = saveUserPhoto(photo, user.uid)
                mDbRef.child("user").child(user!!.uid).setValue(User(name, username, user.uid,photoName)).await()
                Resource.Success(true)
            }else{
                Resource.Error("Não há usuários para inserir")
            }
        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun updateToken(token: String?): Resource<Boolean>? {
        return try {
            val user = firebaseAuth.currentUser
            if(user != null){
                val usuario = mutableMapOf<String, Any?>()
                usuario.put("token",token)
                mDbRef.child("user").child(user!!.uid).updateChildren(usuario).await()
                Resource.Success(true)
            }else{
                Resource.Error("Não há usuários para inserir o token")
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun updateUser(name: String, photo: Bitmap): Resource<Boolean>? {
        return try {
            val user = firebaseAuth.currentUser
            if(user != null){
                deletePhoto(user.uid)
                val namePhoto: String? = saveUserPhoto(photo,user.uid)
                val usuario = mutableMapOf<String, Any?>()
                usuario.put("name",name)
                usuario.put("photoName",namePhoto)
                mDbRef.child("user").child(user!!.uid).updateChildren(usuario).await()
                Resource.Success(true)
            }else{
                Resource.Error("Não há usuários para inserir")
            }
        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    private suspend fun saveUserPhoto(bitmap: Bitmap, uidUser:String): String?{
        val storageRef = firebaseStorage.getReference()
        val mountainImagesRef = storageRef.child("usuario/$uidUser.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = mountainImagesRef.putBytes(data).await()

        if(uploadTask.bytesTransferred > 0){
            return "usuario/$uidUser.jpg"
        }else{
            return null
        }
    }

    private suspend fun saveMessagePhoto(bitmap: Bitmap, photoName:String): String?{
        val storageRef = firebaseStorage.getReference()
        val mountainImagesRef = storageRef.child("messages/$photoName.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = mountainImagesRef.putBytes(data).await()

        if(uploadTask.bytesTransferred > 0){
            return "messages/$photoName.jpg"
        }else{
            return null
        }
    }

    override suspend fun insertMessage(
        receiverUid:String,
        message: String
    ): Resource<Boolean>? {
        return try {
            val senderUID = firebaseAuth.currentUser?.uid
            val senderRoom = receiverUid + senderUID
            val receiverRoom = senderUID + receiverUid
            val data = Date().time
            val messageObject = Message(null,message,senderUID, null,null, data)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).await()
            mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject).await()

            val senderChat = mDbRef.child("chats").child(senderRoom!!).get().await().getValue(Chat::class.java)

            if(senderChat!!.unreadMessages != null){
                val messagesNotRead = senderChat.unreadMessages!! + 1
                val value = mutableMapOf<String, Any?>()
                value.put("unreadMessages",messagesNotRead)
                mDbRef.child("chats").child(senderRoom!!).updateChildren(value).await()
            }else{
                val value = mutableMapOf<String, Any?>()
                value.put("unreadMessages",0)
                mDbRef.child("chats").child(senderRoom!!).updateChildren(value).await()
            }


            Resource.Success(true)
        }catch(e: Exception){
            Resource.Error(e.message)
        }


    }

    override suspend fun insertFotoMessage(receiverUid: String, photo: Bitmap): Resource<Boolean>? {
        return try {
            val senderUID = firebaseAuth.currentUser?.uid
            val senderRoom = receiverUid + senderUID
            val receiverRoom = senderUID + receiverUid
            val data = Date().time


            val photoName:String = "PHOTO_"+System.currentTimeMillis()
            val namePhoto: String? = saveMessagePhoto(photo,photoName)

            val messageObject = Message(null,null,senderUID, null,namePhoto, data)

            mDbRef.child("chats").child(senderRoom).child("messages").push().setValue(messageObject).await()
            mDbRef.child("chats").child(receiverRoom).child("messages").push().setValue(messageObject).await()


            val senderChat = mDbRef.child("chats").child(senderRoom).get().await().getValue(Chat::class.java)
            if(senderChat!!.unreadMessages != null){
                val messagesNotRead = senderChat.unreadMessages!! + 1
                val value = mutableMapOf<String, Any?>()
                value.put("unreadMessages",messagesNotRead)
                mDbRef.child("chats").child(senderRoom!!).updateChildren(value).await()
            }else{
                val value = mutableMapOf<String, Any?>()
                value.put("unreadMessages",0)
                mDbRef.child("chats").child(senderRoom!!).updateChildren(value).await()
            }



            Resource.Success(true)
        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    override suspend fun listChats(): Resource<List<Chat>>? {
        return try {
            val senderUID = firebaseAuth.currentUser?.uid
            val users = mDbRef.child("user").get().await()
            if(users.exists()){
                val u = ArrayList<Chat>()
                for(postSnapshot in users.children){
                    var userReceiver = postSnapshot.getValue(User::class.java)
                    if(!userReceiver!!.uid.equals(senderUID)){
                        val receiverRoom = senderUID + userReceiver.uid

                        val chat = mDbRef.child("chats").child(receiverRoom).get().await().getValue(Chat::class.java)
                        if(chat != null){
                            if(userReceiver.photoName != null){
                                userReceiver.photoName = searchFotoUrl(userReceiver.photoName!!)
                            }
                            val chat = Chat(userReceiver.name!!,userReceiver.uid!!, userReceiver.photoName, unreadMessages = chat.unreadMessages)
                            u.add(chat)
                        }
                    }

                }
                Resource.Success(u)
            }else{
                Resource.Error("")
            }
        }catch (e:Exception){
            Resource.Error(e.message)
        }


    }

    override suspend fun listMessages(receiverUid: String): Flow<Resource<List<Message>>>? = callbackFlow {
        // 2.- We create a reference to our data inside Firestore
        val senderUID = firebaseAuth.currentUser?.uid
        val senderRoom = receiverUid + senderUID
        val receiverRoom = senderUID + receiverUid
        val eventDocument =  mDbRef.child("chats").child(senderRoom!!).child("messages")
        val receiver = mDbRef.child("user").child(receiverUid).get().await()
        var userReceiver = receiver.getValue(User::class.java)
        if(userReceiver?.photoName != null){
            userReceiver.photoName = searchFotoUrl(userReceiver.photoName!!)
        }

        val value = mutableMapOf<String, Any?>()
        value.put("unreadMessages",0)
        mDbRef.child("chats").child(receiverRoom!!).updateChildren(value).await()

        // 3.- We generate a subscription that is going to let us listen for changes with
        // .addSnapshotListener and then offer those values to the channel that will be collected in our viewmodel
        val subscription = eventDocument.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val m = ArrayList<Message>()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        message?.senderId?.let {
                            if(senderUID != receiverUid && userReceiver?.photoName != null){
                                message.photoUserSender = userReceiver.photoName
                            }
                            m.add(message)
                        }
                    }
                }
                trySend(Resource.Success(m))
               // close()

            }


            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message.toString(), null))
            }



        })

         //mDbRef.addValueEventListener(subscription)


        //Finally if collect is not in use or collecting any data we cancel this channel to prevent any leak and remove the subscription listener to the database

        awaitClose {
            mDbRef.removeEventListener(subscription)
        }

    }

    override suspend fun searchFotoUrl(path: String): String {
        val storageRef = firebaseStorage.getReference()
        val ref = storageRef.child(path)
        return ref.downloadUrl.await().toString()
    }
}