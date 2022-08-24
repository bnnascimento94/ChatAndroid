package com.example.chatandroid

import android.content.Context
import android.text.InputType.TYPE_NULL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context, val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED = 1
    val ITEM_SENT =2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate received message
            val view:View = LayoutInflater.from(context).inflate(R.layout.received_message,parent,false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent_message,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            // do stuff for sent holder
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMessage.message
            viewHolder.sentMessage.isFocusableInTouchMode = false
            viewHolder.sentMessage.inputType = TYPE_NULL
        }else{
            // do stuff for receive holder
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receivedMessage.text = currentMessage.message
            viewHolder.receivedMessage.isFocusable = false
            viewHolder.receivedMessage.isFocusableInTouchMode = false
            viewHolder.receivedMessage.inputType = TYPE_NULL

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
        val sentMessage = itemView.findViewById<TextView>(R.id.txtSent)

    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.findViewById<TextView>(R.id.txtReceived)

    }


}