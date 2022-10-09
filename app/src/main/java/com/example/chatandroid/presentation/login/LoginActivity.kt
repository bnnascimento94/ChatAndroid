package com.example.chatandroid.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.R
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityLoginBinding
import com.example.chatandroid.presentation.chat.chatList.ChatsActivity
import com.example.chatandroid.presentation.chat.chatList.ChatsAdapter
import com.example.chatandroid.presentation.users.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: LoginViewModelFactory

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    private lateinit var progressBar : ProgressBar
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnSignUp: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel = ViewModelProvider(this,factory).get(LoginViewModel::class.java)

        editEmail = binding.txtLogin
        editPassword = binding.txtSenha
        btnLogin = binding.btnLogar
        btnSignUp = binding.btnCadastrar
        progressBar = binding.progress

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            login(email,password)
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.loginUser.observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    startActivity(Intent(this@LoginActivity, ChatsActivity::class.java))
                }
            }

        })

    }

    private fun login(email: String, password: String) {
        if(email.isNotBlank() && password.isNotBlank()){
            loginViewModel.logarUsuario(email,password)
        }else{
            Toast.makeText(this,"Preencha os campos", Toast.LENGTH_LONG).show()
        }

    }
}