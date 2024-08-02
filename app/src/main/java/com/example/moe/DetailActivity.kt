package com.example.moe

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.moe.databinding.ActivityDetailBinding

class DetailActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        val search: Search = intent.getParcelableExtra("search")!!

        Glide.with(this)
            .load(search.photo)
            .into(binding.detailImg)

        binding.detailTitle.text = search.title
        val date = search.startDate.substring(0, 10).replace("-", ".") + " ~ " + search.endDate.substring(0,10).replace("-", ".")
        binding.dateTv.text = date

        binding.recordBtn.setOnClickListener {
            TODO()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}