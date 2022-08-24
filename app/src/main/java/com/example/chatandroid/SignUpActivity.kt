package com.example.chatandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var editName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnSignUp: MaterialButton
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        editName = findViewById<TextInputEditText>(R.id.txtName)
        editEmail = findViewById<TextInputEditText>(R.id.txtLogin)
        editPassword = findViewById<TextInputEditText>(R.id.txtSenha)
        btnSignUp = findViewById(R.id.btnCadastrar)

        btnSignUp.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()
            
            signUpUser(name,email,password)
        }
    }

    private fun signUpUser(name: String, email: String, password: String) {
        if(!name.isBlank() && !email.isBlank() && !password.isBlank()){
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = mAuth.currentUser
                        addUserToDatabase(name,email,user?.uid!!)
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{

                        Toast.makeText(this@SignUpActivity,task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this@SignUpActivity,"Preencha os campos corretamente", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}