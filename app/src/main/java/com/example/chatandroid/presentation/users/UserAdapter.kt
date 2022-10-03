package com.example.chatandroid.presentation.users

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
import com.example.chatandroid.data.model.User
import com.example.chatandroid.presentation.chat.ChatActivity

class UserAdapter(val context: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var userlist: ArrayList<User> = ArrayList()

    public fun load(userlist:ArrayList<User>){
        this.userlist = userlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val currentUser = userlist[position]
        holder.textName.text = currentUser.name
        holder.email.text = currentUser.email
        if(currentUser.photoName != null){
            Glide
                .with(holder.imageView.context)
                .load(currentUser.photoName)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.imageView);
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser?.uid)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }


    class UserViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
       val textName = itemview.findViewById<TextView>(R.id.txtNome)
       val email = itemview.findViewById<TextView>(R.id.txtEmail)
       val imageView = itemView.findViewById<ImageView>(R.id.userphoto)
    }

}