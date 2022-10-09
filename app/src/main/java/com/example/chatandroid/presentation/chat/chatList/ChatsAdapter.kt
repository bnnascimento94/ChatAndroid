package com.example.chatandroid.presentation.chat.chatList

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
import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.presentation.chat.messages.ChatActivity

class ChatsAdapter(val context: Context): RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>()  {

    var chatlist: ArrayList<Chat> = ArrayList()

    public fun load(chatlist:ArrayList<Chat>){
        this.chatlist = chatlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout,parent,false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentChat = chatlist[position]
        holder.textName.text = currentChat.userName
        var unreadMessages = 0
        if(currentChat.unreadMessages != null){
            unreadMessages = currentChat.unreadMessages
        }else {
            unreadMessages = 0
        }
        holder.email.text = unreadMessages.toString()
        if(currentChat.photoUserUrl != null){
            Glide
                .with(holder.imageView.context)
                .load(currentChat.photoUserUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.imageView);
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentChat.userName)
            intent.putExtra("uid", currentChat?.userUid)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return chatlist.size
    }

    class ChatViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val textName = itemview.findViewById<TextView>(R.id.txtNome)
        val email = itemview.findViewById<TextView>(R.id.txtEmail)
        val imageView = itemView.findViewById<ImageView>(R.id.userphoto)
    }


}