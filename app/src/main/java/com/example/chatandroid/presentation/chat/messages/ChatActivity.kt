package com.example.chatandroid.presentation.chat.messages

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatandroid.R
import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityChatBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: MessageAdapter

    @Inject
    lateinit var factory: ChatActivityViewModelFactory

    lateinit var chatActivityViewModel: ChatActivityViewModel

    lateinit var binding: ActivityChatBinding

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: TextInputEditText
    private lateinit var sendMessage: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        chatActivityViewModel = ViewModelProvider(this,factory).get(ChatActivityViewModel::class.java)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar!!)
        supportActionBar?.title = name

        messageRecyclerView = binding.rv
        messageBox = binding.txtMessage
        sendMessage = binding.btnSend
        val layoutManager = LinearLayoutManager(this)
        messageRecyclerView.layoutManager = layoutManager
        messageRecyclerView.adapter = adapter


        //logic for adding data to recycler view
        chatActivityViewModel.getMessages(receiverUid!!).observe(this, androidx.lifecycle.Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    binding.progress.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@ChatActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    binding.progress.visibility = View.GONE
                    adapter.load(resource.data as ArrayList<Message>)
                    messageRecyclerView.smoothScrollToPosition(resource.data.size -1)
                }
            }

        })

        // adding the message to database
        sendMessage.setOnClickListener {
            val message = messageBox.text.toString().trim()
            if(!message.isBlank()){
                chatActivityViewModel.sendMessages(receiverUid,message)
                messageBox.setText("")
            }

        }
    }
}