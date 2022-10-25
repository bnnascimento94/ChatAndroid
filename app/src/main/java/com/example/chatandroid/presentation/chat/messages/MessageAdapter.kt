package com.example.chatandroid.presentation.chat.messages

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatandroid.R
import com.example.chatandroid.data.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context:Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var nome:String? = ""
    var messageList: ArrayList<Message> = ArrayList()

    public fun load(messageList:ArrayList<Message>, nome:String?){
        this.messageList = messageList
        this.nome = nome
        this.notifyDataSetChanged()
    }


    val ITEM_RECEIVED = 1
    val ITEM_SENT =2
    val PHOTO_SENT = 3
    val PHOTO_RECEIVED = 4
    val DOC_SENT = 5
    val DOC_RECEIVED = 6
    val DATA_GROUP = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == ITEM_RECEIVED){
            //inflate received message
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.received_message,parent,false)
            return ReceiveViewHolder(view)
        }else if(viewType == ITEM_SENT){
            //inflate sent
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.sent_message,parent,false)
            return SentViewHolder(view)
        }else if(viewType == PHOTO_SENT){
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.sent_photo,parent,false)
            return SentPhotoViewHolder(view)
        }else if(viewType == DATA_GROUP){
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.data_item_message,parent,false)
            return DataViewHolder(view)
        } else{
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.received_photo,parent,false)
            return ReceivedPhotoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            // do stuff for sent holder
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.setText(currentMessage.message)
            viewHolder.sentMessage.isFocusableInTouchMode = false
            currentMessage.time?.let {
                viewHolder.time.text = currentMessage.dataTexto
            }

            //viewHolder.sentMessage.inputType = TYPE_NULL

        }else if(holder.javaClass == SentPhotoViewHolder::class.java){
            val viewHolder = holder as SentPhotoViewHolder
            if(currentMessage.photoMessage != null){
                Glide
                    .with(viewHolder.imageView.context)
                    .load(currentMessage.photoMessage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(viewHolder.imageView);
            }

            currentMessage.time?.let {
                viewHolder.time.text = currentMessage.dataTexto
            }

        }else if(holder.javaClass == ReceivedPhotoViewHolder::class.java){
            val viewHolder = holder as ReceivedPhotoViewHolder
            if(currentMessage.photoMessage != null){
                Glide
                    .with(viewHolder.receivedPhoto.context)
                    .load(currentMessage.photoMessage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(viewHolder.receivedPhoto);
            }

            if(currentMessage.photoUserSender != null){
                Glide
                    .with(holder.imageView.context)
                    .load(currentMessage.photoUserSender)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(holder.imageView);
            }

            currentMessage.time?.let {
                viewHolder.time.text = currentMessage.dataTexto
            }

        }else if(holder.javaClass == DataViewHolder::class.java){
            val viewHolder = holder as DataViewHolder
            viewHolder.data.text = currentMessage.messageTitledDate
        } else{
            // do stuff for receive holder
            val viewHolder = holder as ReceiveViewHolder
            if(currentMessage.photoUserSender != null){
                    Glide
                        .with(holder.imageView.context)
                        .load(currentMessage.photoUserSender)
                        .centerCrop()
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(holder.imageView);
            }
            viewHolder.receivedMessage.setText(currentMessage.message)
            viewHolder.receivedMessage.isFocusable = false
            viewHolder.receivedMessage.isFocusableInTouchMode = false

            currentMessage.time?.let {
                viewHolder.time.text = currentMessage.dataTexto
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(currentMessage.messageTitledDate != null){
            DATA_GROUP
        } else if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            if(currentMessage.photoMessage != null){
                PHOTO_SENT
            }else{
                ITEM_SENT
            }

        }else{
            if(currentMessage.photoMessage != null){
                PHOTO_RECEIVED
            }else{
                ITEM_RECEIVED
            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.txtSent)
        val time: TextView = itemView.findViewById(R.id.txtTime)

    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById(R.id.txtReceived)
        val imageView = itemView.findViewById<ImageView>(R.id.userphoto)
        val time: TextView = itemView.findViewById(R.id.txtTime)

    }


   inner class SentPhotoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.photoSent)
       val time: TextView = itemView.findViewById(R.id.txtTime)
       init {
           itemView.setOnClickListener {
               val currentMessage = messageList[position]
               val intent = Intent(context,ImagePreviewActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("nome", nome)
               intent.putExtra("url", currentMessage.photoMessage)
               context.startActivity(intent)
           }
       }


    }

    inner class ReceivedPhotoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedPhoto: ImageView = itemView.findViewById(R.id.photoReceived)
        val time: TextView = itemView.findViewById(R.id.txtTime)
        val imageView = itemView.findViewById<ImageView>(R.id.userphoto)
        init {
            itemView.setOnClickListener {
                val currentMessage = messageList[position]
                val intent = Intent(context,ImagePreviewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("nome", nome)
                intent.putExtra("url", currentMessage.photoMessage)
                context.startActivity(intent)
            }
        }

    }

    inner class DataViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val data: TextView = itemView.findViewById(R.id.txtData)

    }


}