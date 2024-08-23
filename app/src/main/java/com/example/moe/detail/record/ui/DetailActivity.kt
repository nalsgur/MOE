package com.example.moe.detail.record.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ActivityDetailBinding
import com.example.moe.databinding.RecordDialogBinding
import com.example.moe.detail.record.remote.RecordService
import com.example.moe.detail.record.entities.Photo
import com.example.moe.detail.record.entities.RecordPage
import com.example.moe.detail.record.remote.RecordViewModel
import com.example.moe.detail.search.entities.Search

class DetailActivity() : AppCompatActivity(), ConfirmDialogInterface {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var search: Search
    private val recordViewModel: RecordViewModel by viewModels()
    private val photo: MutableList<String> = mutableListOf()

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        results.entries.forEach{
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
                //val recordService = RecordService()
                //val imageUris = mutableListOf<String>()
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    photo.add(imageUri.toString())
                }
                //val photo = Photo(imageUris)
//                recordService.uploadPhoto(search.id, photo)
                recordViewModel.setRecordPage(search, photo)
//                recordViewModel.getRecordPage(1, search.id)
            } else if (data?.data != null) {
                // 단일 이미지를 선택한 경우
                val imageUri = data.data
                Log.d("DETAIL_ACTIVITY", "Selected URI: ${imageUri.toString()}")
//                val photo = Photo(listOf(imageUri.toString()))
                photo.add(imageUri.toString())
//                val recordService = RecordService()
//                recordService.uploadPhoto(search.id, photo)
                recordViewModel.setRecordPage(search, photo)
//                recordViewModel.getRecordPage(1, search.id)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        search = intent.getParcelableExtra("search")!!

        //recordViewModel.getRecordPage(1, search.id)
        if (search.id == 6 || search.id== 40){
            photo.addAll(
                listOf(
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/21a6ef66-20c0-4c86-b786-8adb3165d285/image.jpeg",
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/874ed1a5-19c2-4917-a38f-fd7238ea74d9/image.jpeg",
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/e3a37998-2496-4f0c-8dfe-2266df7a8554/image.jpeg",
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/bf1a8edc-4ac4-44cc-85f5-8b1015b06a05/image.jpeg",
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/f13c6a74-b426-4ddd-938d-3927342c5022/image.jpeg",
                    "https://velog.velcdn.com/images/wjdwlsdl321/post/f9967e97-9b66-4587-9e38-993b6b24e703/image.jpeg"
                )
            )
            recordViewModel.setRecordPage(search, photo)
        }

        Glide.with(this)
            .load(search.photo)
            .into(binding.detailImg)

        binding.detailTitle.text = search.title
        val date = search.startDate.substring(0, 10).replace("-", ".") + " ~ " + search.endDate.substring(0,10).replace("-", ".")
        binding.dateTv.text = date

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
        recordViewModel.recordPageState.observe(this){
            if(it.response !== null){
                binding.recordBtnTv.setText(R.string.addPhoto)
                binding.caution.visibility = View.GONE
                binding.line.visibility = View.GONE
                binding.photoRv.visibility = View.VISIBLE
                recordedInitView(it.response)
            }else{
                binding.recordBtnTv.setText(R.string.detail_record)
                binding.caution.visibility = View.VISIBLE
                binding.line.visibility = View.VISIBLE
                binding.photoRv.visibility = View.GONE
                notRecordedInitView()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recordedInitView(response: RecordPage) {
        binding.recordBtn.setOnClickListener {
            requestStorage()
        }

        val photoAdapter = PhotoRVAdapter(response.recordPhoto!!)
        binding.photoRv.layoutManager =
            GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        binding.photoRv.adapter = photoAdapter

        recordViewModel.recordPageState.observe(this){
            photoAdapter.notifyDataSetChanged()
        }

        photoAdapter.setMyItemClickListener(object : PhotoRVAdapter.MyItemClickListener{
            override fun onClickItem(position: Int) {
                val intent = Intent(this@DetailActivity, WriteActivity::class.java)
                intent.putExtra("recordPhotoId", position)
                intent.putExtra("response", response)
                //intent.putExtra("recordPageId", search.id)
                startActivity(intent)
            }
        })
    }

    private fun notRecordedInitView(){
        binding.recordBtn.setOnClickListener {
            val dialog = ConfirmDialog(this)
            dialog.isCancelable = false
            dialog.show(this.supportFragmentManager, "RecordDialog")
        }
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