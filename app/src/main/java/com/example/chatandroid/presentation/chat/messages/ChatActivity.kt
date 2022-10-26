package com.example.chatandroid.presentation.chat.messages

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
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatandroid.R
import com.example.chatandroid.data.model.Message
import com.example.chatandroid.data.util.ImageSaver
import com.example.chatandroid.data.util.Resource
import com.example.chatandroid.databinding.ActivityChatBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: MessageAdapter

    @Inject
    lateinit var factory: ChatActivityViewModelFactory

    lateinit var chatActivityViewModel: ChatActivityViewModel

    lateinit var binding: ActivityChatBinding

    lateinit var callback: BottomChooseFileFragment.Callback

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: TextInputEditText
    private lateinit var sendMessage: ImageButton
    private lateinit var receiverUid: String

    companion object {
        private const val SOLICITAR_PERMISSAO = 1
    }

    private var resultadoTirarFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                chatActivityViewModel.sendPhotoMessage(receiverUid,chatActivityViewModel.rotacionarImagem())
            } catch (e: Exception) {
                Toast.makeText(applicationContext, " $e", Toast.LENGTH_LONG)
            }
        }
    }


    var launchGaleriaFoto = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
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
                    chatActivityViewModel.sendPhotoMessage(receiverUid,selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        chatActivityViewModel = ViewModelProvider(this,factory).get(ChatActivityViewModel::class.java)

        val name = intent.getStringExtra("name")
        receiverUid = intent.getStringExtra("uid").toString()

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
                    resource.data?.let {
                        if (it.isNotEmpty()){
                            adapter.load(it as ArrayList<Message>,name)
                        }
                    }

                    messageRecyclerView.smoothScrollToPosition(resource.data?.size!! -1)
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

        binding.btnSendFile.setOnClickListener {
            val bottomChooseFileFragment = BottomChooseFileFragment(object: BottomChooseFileFragment.Callback{
                override fun onCamera() {
                    val permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    val checkCameraPermission = ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.CAMERA)
                    val permissionRead = ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), SOLICITAR_PERMISSAO
                        )
                    } else if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.CAMERA
                            ), SOLICITAR_PERMISSAO
                        )
                    } else if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), SOLICITAR_PERMISSAO
                        )
                    } else {
                        tirarFoto()
                    }
                }

                override fun onGaleria() {
                    val permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    val checkCameraPermission = ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.CAMERA)
                    val permissionRead = ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), SOLICITAR_PERMISSAO
                        )
                    } else if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.CAMERA
                            ), SOLICITAR_PERMISSAO
                        )
                    } else if (permissionRead != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@ChatActivity, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), SOLICITAR_PERMISSAO
                        )
                    } else {
                        buscarGaleria()
                    }
                }


            })

            bottomChooseFileFragment.show(supportFragmentManager, "tag")
        }



    }


    override fun onDestroy() {
        super.onDestroy()
        adapter.load(ArrayList<Message>(),"")
    }


    private fun tirarFoto(){
        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        try {
            chatActivityViewModel?.filePhoto = ImageSaver.createImageFile(ChatActivity@this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                this.packageName + ".provider", chatActivityViewModel?.filePhoto!!
            )
        )
        resultadoTirarFoto.launch(intent)
    }

    private fun buscarGaleria(){
        val intent = Intent()
        intent.setType("image/*")
        intent.action = Intent.ACTION_GET_CONTENT
        launchGaleriaFoto.launch(intent)
    }
}