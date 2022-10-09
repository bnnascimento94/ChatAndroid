package com.example.chatandroid.presentation.chat.messages

import android.content.Context
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

class MessageAdapter(val context:Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var messageList: ArrayList<Message> = ArrayList()

    public fun load(messageList:ArrayList<Message>){
        this.messageList = messageList
        this.notifyDataSetChanged()
    }


    val ITEM_RECEIVED = 1
    val ITEM_SENT =2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate received message
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.received_message,parent,false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.sent_message,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            // do stuff for sent holder
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.setText(currentMessage.message)
            viewHolder.sentMessage.isFocusableInTouchMode = false
            //viewHolder.sentMessage.inputType = TYPE_NULL

        }else{
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
            //viewHolder.receivedMessage.inputType = TYPE_NULL
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            ITEM_SENT
        }else{
            ITEM_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.txtSent)

    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById(R.id.txtReceived)
        val imageView = itemView.findViewById<ImageView>(R.id.userphoto)
    }


}