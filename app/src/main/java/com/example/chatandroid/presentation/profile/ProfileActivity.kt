package com.example.chatandroid.presentation.profile

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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatandroid.R
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ProfileViewModelFactory
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding

    companion object {
        private const val SOLICITAR_PERMISSAO = 1
    }

    private var resultadoTirarFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                binding?.logo?.setImageBitmap(profileViewModel?.rotacionarImagem())
            } catch (e: Exception) {
                Toast.makeText(applicationContext, " $e", Toast.LENGTH_LONG)
            }
        }
    }


    var launchSomeActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
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

                    profileViewModel.filePhoto = File(selectedImageUri?.path)
                    binding?.logo?.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileViewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        profileViewModel.getUser()


        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Perfil"


        profileViewModel.getUser.observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    binding.progress.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@ProfileActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    binding.progress.visibility = View.GONE
                    val t = resource?.data?.name
                    binding.txtName.setText(resource?.data?.name)
                    if(resource.data?.photoName != null){
                        Glide
                            .with(binding.logo.context)
                            .load(resource.data?.photoName)
                            .centerCrop()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .into(binding.logo)
                    }
                }
            }
        })

        profileViewModel.updateUser.observe(this, Observer { resource ->
            when(resource){
                is Resource.Loading ->{
                    binding.progress.visibility = View.VISIBLE
                }
                is Resource.Error ->{
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@ProfileActivity,resource.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Success ->{
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@ProfileActivity,"Usuário atualizado", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnCarregarFoto.setOnClickListener {

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

        binding.btnAtualizar.setOnClickListener {
            val name = binding.txtName.text.toString()

            if(profileViewModel.filePhoto == null){
                Toast.makeText(this@ProfileActivity,"Selecione a Foto do Usuário", Toast.LENGTH_SHORT).show()
            } else if(!name.isBlank()){
                profileViewModel.updateUser(name, binding.logo.drawToBitmap())
            }else{
                Toast.makeText(this@ProfileActivity,"Preencha os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }

    }



    private fun tirarFoto(){
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        try {
            profileViewModel?.filePhoto = ImageSaver.createImageFile(SignUpActivity@this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                this.packageName + ".provider", profileViewModel?.filePhoto!!
            )
        )
        resultadoTirarFoto.launch(intent)
    }

    private fun buscarGaleria(){
        val intent = Intent()
        intent.setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(intent)
    }

}