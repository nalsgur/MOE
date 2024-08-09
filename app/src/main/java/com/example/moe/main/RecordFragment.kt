package com.example.moe.main

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moe.ConfirmDialogInterface
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.Search
import com.example.moe.SearchActivity
import com.example.moe.databinding.FragmentRecordBinding
import com.example.moe.databinding.RecordDialogBinding
import com.example.moe.detail.WriteFragment
import com.example.moe.record.Photo
import com.example.moe.record.RecordService
import com.example.moe.record.RecordViewModel

class RecordFragment() : Fragment(), ConfirmDialogInterface {
    private lateinit var binding: FragmentRecordBinding
    private lateinit var search: Search
    private lateinit var recordImg: List<ImageView>
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
                Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_LONG).show()
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
                val photo = Photo(listOf(imageUri.toString()))
                val recordService = RecordService()
                recordService.uploadPhoto(search.id, photo)
                requireActivity().finishAffinity() //MainActivity로 돌아감 MainActivity에서 기록 프래그먼트로 전환해야 함
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        // SearchResponse에서 null값 참조하지 못해서 임의로 default 값 설정함
        search = requireActivity().intent.getParcelableExtra("search") ?: Search.default()

        // SearchRespone의 이미지가 null일 경우 더미 데이터 사용
        val imageUrl = search.photo ?: getSmallImageUrls().first()

        Glide.with(this)
            .load(search.photo)
            .into(binding.detailImg)

        binding.detailTitle.text = search.title
        val date = search.startDate.substring(0, 10).replace("-", ".") + " ~ " + search.endDate.substring(0,10).replace("-", ".")
        binding.dateTv.text = date

        // 버튼 추가 바람
//        binding.recordBtn.setOnClickListener {
//            val dialog = ConfirmDialog(this)
//            dialog.isCancelable = false
//            dialog.show(parentFragmentManager, "RecordDialog")
//        }

        setUpRecordImg()

        binding.backBtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }

        binding.searchIcon.setOnClickListener{
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        return binding.root
    }

    private fun setUpRecordImg() {
        recordImg = listOf(
            binding.recordImg1, binding.recordImg2, binding.recordImg3, binding.recordImg4,
            binding.recordImg5, binding.recordImg6, binding.recordImg7, binding.recordImg8
        )

        recordImg.forEach { imageView ->
            imageView.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("imageId", imageView.id)
                    putStringArray("smallImages", getSmallImageUrls())
                }

                val writeFragment = WriteFragment().apply {
                    arguments = bundle
                }

                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                val recordFragment = fragmentManager.findFragmentByTag("diary")
                recordFragment?.let {
                    fragmentTransaction.hide(it)
                }

                if (fragmentManager.findFragmentByTag("writeFragment") == null) {
                    fragmentTransaction.add(R.id.main_frm, writeFragment, "writeFragment")
                } else {
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("writeFragment")!!)
                }

                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

    }

    // WriteFragment의 작은 이미지 더미 데이터
    private fun getSmallImageUrls(): Array<String> {
        return arrayOf(
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=928746",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=926114",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=925407",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=924623",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=926226",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=928459",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=927584",
            "https://sema.seoul.go.kr/common/imgFileView?FILE_ID=927248"
        )
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


// DetailActivity의 클래스와 중복됨, 패키지 경로를 다르게 해서 해결했으나 주의 필요함
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