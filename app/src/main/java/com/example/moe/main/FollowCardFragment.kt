package com.example.moe.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.SearchActivity
import com.example.moe.databinding.FragmentFollowCardBinding


class FollowCardFragment : Fragment() {
    private lateinit var binding : FragmentFollowCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowCardBinding.inflate(inflater, container, false)

        //뒤로가기 버튼 클릭시
        binding.followCardBackbtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }
        //검색 버튼 클릭시
        binding.followCardSearchbtn.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        //마이페이지 버튼 클릭시
        binding.followCardMypagebtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_mypage
        }
        //카드모드 클릭시
        binding.followCardChangemodbtn.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.main_frm, FollowFragment()).commit()
        }






        return binding.root
    }
}