package com.example.chatandroid.presentation.users

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatandroid.R
import com.example.chatandroid.data.model.User
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityMainBinding
import com.example.chatandroid.presentation.login.LoginActivity
import com.example.chatandroid.presentation.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: UserAdapter

    @Inject
    lateinit var factory: MainActivityViewModelFactory

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)
        mainActivityViewModel.getUsers()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Contatos"


        userRecyclerView = binding.rv
        progressBar = binding.progress
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mainActivityViewModel.getUsers.observe(this, androidx.lifecycle.Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    adapter.load(resource.data as ArrayList<User>)
                }
            }

        })

        mainActivityViewModel.logoutUser.observe(this, androidx.lifecycle.Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            mainActivityViewModel.logoutUsuario()
        }else if (item.itemId == R.id.profile){
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
        return true
    }

}