package com.example.chatandroid.presentation.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.R
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityLoginBinding
import com.example.chatandroid.presentation.chat.chatList.ChatsActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }


    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginViewModel = ViewModelProvider(this,factory).get(LoginViewModel::class.java)
        askNotificationPermission()

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