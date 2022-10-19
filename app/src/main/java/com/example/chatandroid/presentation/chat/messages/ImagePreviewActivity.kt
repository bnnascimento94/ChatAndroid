package com.example.chatandroid.presentation.chat.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.chatandroid.R
import com.example.chatandroid.databinding.ActivityChatBinding
import com.example.chatandroid.databinding.ActivityImagePreviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePreviewActivity : AppCompatActivity() {

    lateinit var binding: ActivityImagePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview)

        val nome = intent.getStringExtra("nome")
        val photoUrl = intent.getStringExtra("url").toString()

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar!!)
        supportActionBar?.title = nome


        Glide
            .with(binding.imgPreview.context)
            .load(photoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(binding.imgPreview);
    }
}