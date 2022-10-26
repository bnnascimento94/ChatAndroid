package com.example.chatandroid.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.R
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.presentation.chat.chatList.ChatsActivity
import com.example.chatandroid.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: SplashViewModelFactory

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this,factory).get(SplashViewModel::class.java)
        splash()
    }

    private fun splash(){
        Handler().postDelayed({
            splashViewModel.userLoaded().observe(this, Observer { resource ->
                when(resource){
                    is Resource.Loading ->{}
                    is Resource.Error ->{
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    is Resource.Success ->{
                        startActivity(Intent(this@SplashActivity, ChatsActivity::class.java))
                    }
                }

            })
        }, 3000)
    }
}