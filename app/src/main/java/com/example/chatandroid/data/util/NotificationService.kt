package com.example.chatandroid.data.util

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.chatandroid.presentation.chat.chatList.ChatsActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class NotificationService : FirebaseMessagingService() {


    companion object{

    }

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val name: CharSequence  = "Chat Application"
            val description: String = "Notificação Jui Cartaz"
            val importance:Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel : NotificationChannel = NotificationChannel("chat",name, importance)
            channel.description = description

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        if(remote.data.isNotEmpty()){
           val data: Map<String,String> = remote.data
           var titulo: String? = null
           var mensagem:String? = null
           var senderRoom:String? = null
           var receiverRoom:String? = null

            if(data["titulo"] != null){
                titulo = data["titulo"]
            }

           if(data["mensagem"] != null){
               mensagem = data["mensagem"]
           }

            if(data["senderRoom"] != null){
                senderRoom = data["senderRoom"]
            }

            if(data["receiverRoom"] != null){
                receiverRoom = data["receiverRoom"]
            }

            val intent: Intent = Intent(this,ChatsActivity::class.java)
            if(senderRoom != null){
                intent.putExtra("senderRoom",senderRoom)
            }

            if(receiverRoom != null){
                intent.putExtra("receiverRoom", receiverRoom)
            }

            var pendingIntent: PendingIntent? = null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
            }else{
                pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            }


            //val builder :NotificationCompat.Builder = NotificationCompat
             //   .Builder(this,"TESTE")
            //    .setSmallIcon()

        }


    }

}