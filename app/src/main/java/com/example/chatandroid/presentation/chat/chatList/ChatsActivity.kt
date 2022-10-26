package com.example.chatandroid.presentation.chat.chatList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatandroid.R
import com.example.chatandroid.data.model.Chat
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityChatBinding
import com.example.chatandroid.databinding.ActivityChatsBinding
import com.example.chatandroid.presentation.login.LoginActivity
import com.example.chatandroid.presentation.profile.ProfileActivity
import com.example.chatandroid.presentation.users.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: ChatsAdapter

    @Inject
    lateinit var factory: ChatsViewModelFactory

    private lateinit var chatViewModel: ChatsViewModel

    private lateinit var binding: ActivityChatsBinding

    private lateinit var chatsRecyclerView: RecyclerView

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats)
        chatViewModel = ViewModelProvider(this,factory).get(ChatsViewModel::class.java)
        chatViewModel.getChatsList()


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Conversas"


        chatsRecyclerView = binding.rv
        progressBar = binding.progress
        chatsRecyclerView.layoutManager = LinearLayoutManager(this)
        chatsRecyclerView.adapter = adapter

        chatViewModel.chatList.observe(this, Observer { resource->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@ChatsActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    adapter.load(resource.data as ArrayList<Chat>)
                }
            }
        })

        chatViewModel.logoutUser.observe(this, androidx.lifecycle.Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@ChatsActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    val intent = Intent(this@ChatsActivity, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        })


    }

    override fun onResume() {
        super.onResume()
        chatViewModel.getChatsList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            chatViewModel.logoutUsuario()
        }else if (item.itemId == R.id.profile){
            startActivity(Intent(this@ChatsActivity, ProfileActivity::class.java))
        }else if (item.itemId == R.id.contacts){
            startActivity(Intent(this@ChatsActivity, MainActivity::class.java))
        }
        return true
    }
}