package com.example.chatandroid.data.repository.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class DatabaseRespositoryImpl(val mDbRef: DatabaseReference, val firebaseAuth: FirebaseAuth,val firebaseStorage: FirebaseStorage): DatabaseRepository {


    override suspend fun getUserConected(): Resource<User>? {
        return try{
            val user = firebaseAuth.currentUser
            if(user != null){
              val user =  mDbRef.child("user").child(user!!.uid).get().await().getValue(User::class.java)
              if(user?.photoName != null){
                  user!!.photoName = getUrlPhotoProfile(user!!.uid!!)
              }
              Resource.Success(user!!)
            }else{
                Resource.Error("Usuário Não Encontrado!")
            }

        }catch(e: Exception){
            Resource.Error(e.message)
        }
    }

    private suspend fun getUrlPhotoProfile(uid: String): String{
        val storageRef = firebaseStorage.getReference()
        val ref = storageRef.child("usuario/${uid!!}.jpg")
        return ref.downloadUrl.await().toString()
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
                            currentuser!!.photoName = getUrlPhotoProfile(currentuser.uid!!)
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

    override suspend fun insertMessage(
        receiverUid:String,
        message: String
    ): Resource<Boolean>? {
        return try {
            val senderUID = firebaseAuth.currentUser?.uid
            val senderRoom = receiverUid + senderUID
            val receiverRoom = senderUID + receiverUid
            val messageObject = Message(message,senderUID, null)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).await()
            mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject).await()

            Resource.Success(true)
        }catch(e: Exception){
            Resource.Error(e.message)
        }


    }

    override suspend fun listMessages(receiverUid: String): Flow<Resource<List<Message>>>? = callbackFlow {
        // 2.- We create a reference to our data inside Firestore
        val senderUID = firebaseAuth.currentUser?.uid
        val senderRoom = receiverUid + senderUID
        val eventDocument =  mDbRef.child("chats").child(senderRoom!!).child("messages")
        val receiver = mDbRef.child("user").child(receiverUid).get().await()
        var userReceiver = receiver.getValue(User::class.java)
        if(userReceiver?.photoName != null){
            userReceiver.photoName = getUrlPhotoProfile(receiverUid)
        }

        // 3.- We generate a subscription that is going to let us listen for changes with
        // .addSnapshotListener and then offer those values to the channel that will be collected in our viewmodel
        val subscription = eventDocument.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val m = ArrayList<Message>()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        if(message.senderId != receiverUid && userReceiver?.photoName != null){
                            message.photoUserSender = userReceiver.photoName
                        }
                        m.add(message)
                    }
                }
                offer(Resource.Success(m))
               // close()

            }


            override fun onCancelled(error: DatabaseError) {
                Resource.Error(error.message.toString(), null)
                close()
            }

        })



        //Finally if collect is not in use or collecting any data we cancel this channel to prevent any leak and remove the subscription listener to the database
        awaitClose()

    }
}