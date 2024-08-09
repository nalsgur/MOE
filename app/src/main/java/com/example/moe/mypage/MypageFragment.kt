package com.example.moe.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moe.RetrofitService
import com.example.moe.databinding.FragmentMypageBinding
import com.example.moe.mypageServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.ArrayList as ArrayList1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val TAG : String = "에러"

class MypageFragment : Fragment() {
    lateinit var binding : FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container,false)


        binding.mypageBackbtn.setOnClickListener {
            //뒤로가기 버튼을 누르면 홈화면으로 이동??
        }



        //프로필 수정 클릭시 이벤트 처리
        binding.mypageProfileedittv.setOnClickListener(View.OnClickListener() {
            val intent = Intent(activity, Mypage_ProfileEdit_Activity::class.java)
            startActivity(intent)
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

        //닉네임 받아오기
        val retrofit = Retrofit.Builder()
            .baseUrl("https://umc.memoryofexhibition.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.getName().enqueue(object : Callback<ArrayList1<mypageServer>>{
            override fun onResponse(
                call: Call<ArrayList1<mypageServer>>,
                response: Response<ArrayList1<mypageServer>>
            ) {
                if(response.isSuccessful) { //응답이 왔다면
                    val name = response.body()
                    name!!.forEach {

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList1<mypageServer>>, t: Throwable) {
                //not use
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

