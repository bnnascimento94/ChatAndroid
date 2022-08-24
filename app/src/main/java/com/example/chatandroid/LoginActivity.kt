package com.example.chatandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnSignUp: MaterialButton
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        editEmail = findViewById<TextInputEditText>(R.id.txtLogin)
        editPassword = findViewById<TextInputEditText>(R.id.txtSenha)
        btnLogin = findViewById(R.id.btnLogar)
        btnSignUp = findViewById(R.id.btnCadastrar)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            
            login(email,password)
            
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(email: String, password: String) {
        if(!email.isBlank() && !password.isBlank()){
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@LoginActivity,"User doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this,"Preencha os campos", Toast.LENGTH_LONG).show()
        }

    }
}