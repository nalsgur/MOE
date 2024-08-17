package com.example.moe.detail.follow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.moe.databinding.ActivityFollowItemBinding

class FollowItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFollowItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFollowItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("IMAGE_URL")
        val title = intent.getStringExtra("TITLE")
        val date = intent.getStringExtra("DATE")

        Glide.with(this)
            .load(imageUrl)
            .into(binding.followItemImg)

        binding.followItemTitle.text = title
        binding.followItemDate.text = date

        binding.followItemBack.setOnClickListener {
            finish()
        }

    }
}