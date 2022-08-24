package com.example.chatandroid

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: TextInputEditText
    private lateinit var sendMessage: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String? = null
    var senderRoom:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUID = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUID
        receiverRoom = senderUID + receiverUid

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar!!)
        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.rv)
        messageBox = findViewById(R.id.txtMessage)
        sendMessage = findViewById(R.id.btnSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        val layoutManager = LinearLayoutManager(this)
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = messageAdapter


        //logic for adding data to recycler view

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    messageRecyclerView.smoothScrollToPosition(messageList.size -1)
                }


                override fun onCancelled(error: DatabaseError) {

                }

            })


        // adding the message to database
        sendMessage.setOnClickListener {
            val message = messageBox.text.toString().trim()
            if(!message.isBlank()){
                val messageObject = Message(message,senderUID)
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                messageBox.setText("")
            }

        }
    }
}