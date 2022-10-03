package com.example.chatandroid.presentation.login

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatandroid.R
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivitySignUpBinding
import com.example.chatandroid.presentation.users.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: SignUpViewModelFactory

    lateinit var signUpViewModel: SignUpViewModel

    lateinit var binding: ActivitySignUpBinding

    private lateinit var progressBar: ProgressBar
    private lateinit var editName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var confirmPassword : TextInputEditText
    private lateinit var btnSignUp: MaterialButton
    private lateinit var btnAdicionar: MaterialButton

    companion object {
        private const val SOLICITAR_PERMISSAO = 1
    }

    private var resultadoTirarFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                binding?.logo?.setImageBitmap(signUpViewModel?.rotacionarImagem())
            } catch (e: Exception) {
                Toast.makeText(applicationContext, " $e", Toast.LENGTH_LONG)
            }
        }
    }


    var launchSomeActivity = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == RESULT_OK
        ) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri: Uri? = data.data
                val selectedImageBitmap: Bitmap
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImageUri
                    )

                    signUpViewModel.filePhoto = File(selectedImageUri?.path)
                    binding?.logo?.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        signUpViewModel = ViewModelProvider(this,factory).get(SignUpViewModel::class.java)


        progressBar = binding.progress
        editName = binding.txtName
        editEmail = binding.txtLogin
        editPassword = binding.txtSenha
        confirmPassword = binding.txtConfirmarSenha
        btnSignUp = binding.btnCadastrar
        btnAdicionar = binding.btnAdicionar

        signUpViewModel.registerUser.observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@SignUpActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    progressBar.visibility = View.GONE
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                }
            }
        })

        btnSignUp.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()

            if(signUpViewModel.filePhoto == null){
                Toast.makeText(this@SignUpActivity,"Selecione a Foto do Usuário", Toast.LENGTH_SHORT).show()
            } else if(!name.isBlank() && !email.isBlank() && !password.isBlank() && (password.toString() == confirmPassword.text.toString())){
                signUpViewModel.registerUser(name,email,password, binding.logo.drawToBitmap())
            }else{
                Toast.makeText(this@SignUpActivity,"Preencha os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }


        btnAdicionar.setOnClickListener {

            val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val checkCameraPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), SOLICITAR_PERMISSAO
                )
            } else if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA
                    ), SOLICITAR_PERMISSAO
                )
            } else if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), SOLICITAR_PERMISSAO
                )
            } else {
                val builder = AlertDialog.Builder(SignUpActivity@this)
                builder.setTitle("Foto")
                builder.setMessage("Como deseja selecionar a foto")
                builder.setPositiveButton("Tirar foto"){
                    dialog, which -> tirarFoto()
                }
                builder.setNegativeButton("Galeria"){
                        dialog, which -> buscarGaleria()
                }

                builder.show()
            }
        }
    }



    private fun tirarFoto(){
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        try {
            signUpViewModel?.filePhoto = ImageSaver.createImageFile(SignUpActivity@this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                this.packageName + ".provider", signUpViewModel?.filePhoto!!
            )
        )
        resultadoTirarFoto.launch(intent)
    }

    private fun buscarGaleria(){
        val intent = Intent()
        intent.setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(intent)
        //resultadoBuscarGaleria.launch(Intent.createChooser(intent,"Selecione a Foto"))
    }


}