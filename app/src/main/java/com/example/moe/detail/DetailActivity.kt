package com.example.moe

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.moe.databinding.ActivityDetailBinding
import com.example.moe.databinding.RecordDialogBinding
import com.example.moe.record.Photo
import com.example.moe.record.RecordService
import com.example.moe.record.RecordViewModel

class DetailActivity() : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var search: Search
    private val viewModel: RecordViewModel by viewModels()

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.entries.forEach{
            val permissionName = it.key
            val isGranted = it.value
            if(isGranted){
                val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                openGalleryLauncher.launch(pickIntent)
            } else{
                Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val data: Intent? = result.data
            val clipData = data?.clipData

            if (clipData != null && clipData.itemCount > 1) {
                // 여러 장의 이미지를 선택한 경우
                val imageUri = clipData.getItemAt(0).uri
            } else if (data?.data != null) {
                // 단일 이미지를 선택한 경우
                val imageUri = data.data
                Log.d("DETAIL_ACTIVITY", "Selected URI: ${imageUri.toString()}")
                val photo = Photo(listOf(imageUri.toString()))
                Log.d("DETAIL_ACTIVITY", "Photo object: $photo")
                val recordService = RecordService()
                recordService.uploadPhoto(search.id, photo)
                finishAffinity() //MainActivity로 돌아감 MainActivity에서 기록 프래그먼트로 전환해야 함
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        search = intent.getParcelableExtra("search")!!

        Glide.with(this)
            .load(search.photo)
            .into(binding.detailImg)

        binding.detailTitle.text = search.title
        val date = search.startDate.substring(0, 10).replace("-", ".") + " ~ " + search.endDate.substring(0,10).replace("-", ".")
        binding.dateTv.text = date

        binding.recordBtn.setOnClickListener {
            val dialog = ConfirmDialog(this)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "RecordDialog")
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    private fun requestStorage(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    override fun onYesButtonClick() {
        requestStorage()
    }
}

class ConfirmDialog(
    confirmDialogInterface: ConfirmDialogInterface,
) : DialogFragment() {

    private var _binding: RecordDialogBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    init {
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecordDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // 취소 버튼 클릭
        binding.noBtn.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 클릭
        binding.yesBtn.setOnClickListener {
            this.confirmDialogInterface?.onYesButtonClick()
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ConfirmDialogInterface {
    fun onYesButtonClick()
}