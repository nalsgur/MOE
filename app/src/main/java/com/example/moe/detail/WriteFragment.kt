package com.example.moe.detail

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.FragmentWriteBinding

class WriteFragment: Fragment() {
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var smallImageUrls: Array<String>
    private var selectedImageId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteBinding.inflate(inflater,container,false)

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.writeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.writeEndBtn.isEnabled = false
                binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_default)
                binding.writeEndBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.navy))
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEndBtn()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.writeEndBtn.setOnClickListener {
            Toast.makeText(requireContext(), "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
            binding.writeEditText.setText("")
            binding.writeEndBtn.isEnabled = false
            binding.writeEndBtn.setBackgroundResource(R.drawable.write_end_bg_default)
            binding.writeEndBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.navy))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            selectedImageId  = it.getInt("imageId", 0)
            smallImageUrls = it.getStringArray("smallImages") ?: emptyArray()
        }

        val selectedImageUrl = smallImageUrls.getOrNull(selectedImageId) ?: ""
        Glide.with(this)
            .load(selectedImageUrl)
            .into(binding.writeImage)

        val imageViews = listOf(
            binding.imageView1, binding.imageView2, binding.imageView3,
            binding.imageView4, binding.imageView5, binding.imageView6,
            binding.imageView7, binding.imageView8
        )

        imageViews.forEachIndexed { index, imageView ->
            if (index < smallImageUrls.size) {
                Glide.with(this)
                    .load(smallImageUrls[index])
                    .into(imageView)
            } else {
                imageView.setImageDrawable(null)
            }
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
            binding.writeEndBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.navy))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}