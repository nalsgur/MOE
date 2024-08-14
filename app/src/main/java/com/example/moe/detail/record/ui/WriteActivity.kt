package com.example.moe.detail.record.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ActivityWriteBinding
import com.example.moe.detail.record.entities.Memo
import com.example.moe.detail.record.entities.RecordPhoto
import com.example.moe.detail.record.remote.RecordService
import com.example.moe.detail.record.remote.RecordViewModel

class WriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBinding
    private val recordViewModel: RecordViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        //val recordPhotoId = intent.getIntExtra("recordPhotoId", 1)
        //val recordPageId = intent.getIntExtra("recordPageId", 1)
        //recordViewModel.getRecordPhoto(1, recordPageId, recordPhotoId)
        //ecordViewModel.getRecordPage(1, recordPageId)

        val record = RecordPhoto(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4d/Cat_November_2010-1a.jpg/300px-Cat_November_2010-1a.jpg",
            "재미있었다."
        )
        val recordPhoto = listOf(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4d/Cat_November_2010-1a.jpg/300px-Cat_November_2010-1a.jpg"
        )
        initView(record, recordPhoto)


        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.writeEndBtn.isEnabled = false
                binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_default)
                binding.writeEndBtn.setTextColor(ContextCompat.getColor(this@WriteActivity, R.color.navy))
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEndBtn()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.writeEndBtn.setOnClickListener {
            Toast.makeText(this, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
            val recordService = RecordService()
            val memo = Memo(binding.et.text.toString())
//            recordService.record(recordPageId, recordPhotoId, memo)
            recordService.record(1, 1, memo)
            binding.writeEndBtn.isEnabled = false
            binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_default)
            binding.writeEndBtn.setTextColor(ContextCompat.getColor(this, R.color.navy))
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        //getData()
    }

    private fun getData() {
        recordViewModel.recordPhotoState.observe(this){
            when{
                it.loading ->{
                    binding.homeLoadingCt.visibility = View.VISIBLE
                    binding.writeImage.visibility = View.GONE
                    binding.backBtn.visibility = View.GONE
                    binding.et.visibility = View.GONE
                    binding.textBg.visibility = View.GONE
                    binding.photoRv.visibility = View.GONE
                    binding.writeEndBtn.visibility = View.GONE
                }
                it.error != null ->{
                    binding.homeLoadingCt.visibility = View.GONE
                    binding.writeImage.visibility = View.GONE
                    binding.backBtn.visibility = View.VISIBLE
                    binding.et.visibility = View.GONE
                    binding.textBg.visibility = View.GONE
                    binding.photoRv.visibility = View.GONE
                    binding.writeEndBtn.visibility = View.GONE
                    Log.d("error", it.error.toString())
                } else -> {
                    if( it.response != null){
                        binding.homeLoadingCt.visibility = View.GONE
                        binding.writeImage.visibility = View.VISIBLE
                        binding.backBtn.visibility = View.VISIBLE
                        binding.et.visibility = View.VISIBLE
                        binding.textBg.visibility = View.VISIBLE
                        binding.photoRv.visibility = View.VISIBLE
                        binding.writeEndBtn.visibility = View.VISIBLE
                        //initView(it.response)
                    }else{
                        binding.homeLoadingCt.visibility = View.VISIBLE
                        binding.writeImage.visibility = View.GONE
                        binding.backBtn.visibility = View.GONE
                        binding.et.visibility = View.GONE
                        binding.textBg.visibility = View.GONE
                        binding.photoRv.visibility = View.GONE
                        binding.writeEndBtn.visibility = View.GONE
                        Log.d("error", "no response")
                    }
                }
            }
        }
    }

    private fun initView(response: RecordPhoto, photos: List<String>) {
        binding.homeLoadingCt.visibility = View.GONE
        binding.writeImage.visibility = View.VISIBLE
        binding.backBtn.visibility = View.VISIBLE
        binding.et.visibility = View.VISIBLE
        binding.textBg.visibility = View.VISIBLE
        binding.photoRv.visibility = View.VISIBLE
        binding.writeEndBtn.visibility = View.VISIBLE

        //val adapter = PhotoRVAdapter(recordViewModel.recordPageState.value?.response?.recordPhoto!!)
        val adapter = DetailPhotoRVAdapter(photos)
        binding.photoRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.photoRv.adapter = adapter

        Glide.with(this).load(response.recordPhoto).into(binding.writeImage)
        if(response.recordPhotoBody != null){
            binding.et.setText(response.recordPhotoBody)
        }
    }

    private fun checkEndBtn() {
        val writeTx = binding.writeEndBtn.text.toString()

        if (writeTx.isNotEmpty()) {
            binding.writeEndBtn.isEnabled = true
            binding.writeEndBtn.setTextColor(Color.WHITE)
            binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_click)
        } else {
            binding.writeEndBtn.isEnabled = false
            binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_default)
            binding.writeEndBtn.setTextColor(ContextCompat.getColor(this, R.color.navy))
        }
    }

    private fun Activity.hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

}