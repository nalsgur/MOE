package com.example.moe.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.moe.MainAPI.RetrofitClient
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.RetrofitService
import com.example.moe.databinding.FragmentMypageBinding
import com.example.moe.mypage.Mypage_Inputaccount_Activity
import com.example.moe.mypage.Mypage_Introduce_Activity
import com.example.moe.mypage.Mypage_Marketing_Activity
import com.example.moe.mypage.Mypage_Privacy_Activity
import com.example.moe.mypage.Mypage_ProfileEdit_Activity
import com.example.moe.mypage.Mypage_QNA_Activity
import com.example.moe.mypage.Mypage_Use_Activity
import com.example.moe.mypage.mypageAPI.MypageApiService
import com.example.moe.mypage.mypageAPI.MypageResponse
import com.example.moe.mypage.mypageAPI.MypageRetrofitClient
import com.example.moe.mypageServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList as ArrayList1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val TAG : String = "에러"

class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding

    // ActivityResultLauncher 선언
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val newNickname = it.getStringExtra("newNickname")
                // 새로운 닉네임으로 UI 업데이트
                binding.mypageNametv.text = newNickname
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container,false)


        binding.mypageBackbtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }


        //프로필 수정 클릭시 이벤트 처리
        binding.mypageProfileedittv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_ProfileEdit_Activity::class.java)
            startForResult.launch(intent)
        })

        //계정 관리 클릭시 이벤트 처리
        binding.mypageAccounttv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_Inputaccount_Activity::class.java)
            startActivity(intent)
        })

        //자주묻는 질문 클릭시 이벤트처리
        binding.mypageQuestioniv.setOnClickListener (View.OnClickListener(){
            val intent = Intent(activity, Mypage_QNA_Activity::class.java)
            startActivity(intent)
        })
        binding.mypageQuestiontv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_QNA_Activity::class.java)
            startActivity(intent)
        })

        //앱소개글 클릭시 이벤트 처리
         binding.mypageAppIntroduceiv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_Introduce_Activity::class.java)
            startActivity(intent)
        })
        binding.mypageAppIntroducetv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_Introduce_Activity::class.java)
            startActivity(intent)
        })

        //이용약관 보기 클릭시 이벤트 처리
        binding.mypageUseiv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_Use_Activity::class.java)
            startActivity(intent)
        })
        binding.mypageUsetv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_Use_Activity::class.java)
            startActivity(intent)
        })

        //개인정보 처리방침 클릭시 이벤트 처리
        binding.mypagePrivacyiv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_Privacy_Activity::class.java)
            startActivity(intent)
        })
        binding.mypagePrivacytv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_Privacy_Activity::class.java)
            startActivity(intent)
        })

        //마케팅 수집이용동의 클릭시 이벤트 처리
        binding.mypageMarketingiv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_Marketing_Activity::class.java)
            startActivity(intent)
        })
        binding.mypageMarketingtv.setOnClickListener(View.OnClickListener(){
            val intent = Intent(activity, Mypage_Marketing_Activity::class.java)
            startActivity(intent)
        })


        //---------------------------서버연결--------------------------------
        //검색할 키워드
        val keyword = "example"

        //GET 요청으로 닉네임 리스트 받아오기
        MypageRetrofitClient.apiService.mypageNickname(keyword).enqueue(object : Callback<MypageResponse>{
            override fun onResponse(
                call: Call<MypageResponse>,
                response: Response<MypageResponse>
            ) {
                if(response.isSuccessful) {
                    val nickname = response.body()
                    nickname?.forEach{ nickname ->
                        Log.d("mypagenetwork", "Nickname : $nickname")
                    }
                } else {
                    Log.d("mypagenetwork", "Response Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MypageResponse>, t: Throwable) {
                Log.d("mypagenetwork", "API Call Failed: ${t.message}")
            }
        })
        return binding.root
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

