package com.example.moe.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.moe.MainAPI.CombinedAdapter
import com.example.moe.MainAPI.DataResponse
import com.example.moe.MainAPI.ExFilterLatest
import com.example.moe.MainAPI.ExFilterTopLike
import com.example.moe.MainAPI.ExLatestAdapter
import com.example.moe.MainAPI.ExTopLikedAdapter
import com.example.moe.MainAPI.ExhibitionLatest
import com.example.moe.MainAPI.ExhibitionTopLiked
import com.example.moe.MainAPI.FilterResponse
import com.example.moe.MainAPI.PopupFilterLatest
import com.example.moe.MainAPI.PopupFilterTopLike
import com.example.moe.MainAPI.PopupLatestAdapter
import com.example.moe.MainAPI.PopupStoresLatest
import com.example.moe.MainAPI.PopupStoresTopLiked
import com.example.moe.MainAPI.PopupTopLikedAdapter
import com.example.moe.MainAPI.RetrofitClient
import com.example.moe.MainAPI.SharedViewModel
import com.example.moe.R
import com.example.moe.databinding.FragmentFollowBinding
import com.example.moe.databinding.ItemFollowBinding
import com.example.moe.detail.follow.ui.FollowItemActivity
import com.example.moe.detail.record.ui.DetailActivity
import com.example.moe.detail.search.entities.Search
import com.example.moe.detail.search.remote.SearchViewModel
import com.example.moe.detail.search.ui.PageRVAdapter
import com.example.moe.detail.search.ui.SearchActivity
import com.example.moe.detail.search.ui.SearchRVAdapter
import com.example.moe.main.MainAPI.CardItem
import com.example.moe.main.MainAPI.CombinedResponse
import com.example.moe.main.MainAPI.ExFilterLatestAdapter
import com.example.moe.main.MainAPI.ExFilterTopLikedAdapter
import com.example.moe.main.MainAPI.FollowCardAdapter
import com.example.moe.main.MainAPI.FollowCardItem
import com.example.moe.main.MainAPI.PopupFilterLatestAdapter
import com.example.moe.main.MainAPI.PopupFilterTopLikedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class FollowFragment : Fragment() {
    private lateinit var binding : FragmentFollowBinding
    private val apiService by lazy { RetrofitClient.HomeApiService() }
    private lateinit var sharedViewModel: SharedViewModel
    private var isFiltered = false

    private lateinit var combinedAdapter: CombinedAdapter
    private val allCombinedResponses = mutableListOf<CombinedResponse>()

    private lateinit var viewPagerAdapter: FollowCardAdapter
    private val allFollowCardItems = mutableListOf<FollowCardItem>()
    private lateinit var items: List<CardItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater,container,false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        combinedAdapter = CombinedAdapter(sharedViewModel)
        val recyclerview = binding.followRv
        recyclerview.adapter = combinedAdapter
        recyclerview.layoutManager = GridLayoutManager(context,2)

        loadData() //수진님 코드(수정)

        setupViewPager() // CardMode : 추가

        // 상태 유지 flag 추가
        if (savedInstanceState != null) {
            isFiltered = savedInstanceState.getBoolean("isFiltered", false)
        }

        // 카드모드 프레그먼트로 이동하는 로직 : 주석 처리함
//        binding.followChangemode.setOnClickListener {
//            parentFragmentManager.beginTransaction().replace(R.id.main_frm, FollowCardFragment()).commit()
//        }

        // 수진 : 카드모드 변경 로직 추가
        var changemode = false
        binding.followChangemode.setOnClickListener {
            changemode = !changemode
            if(changemode){
                //카드모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode_after)
                binding.followRv.visibility = View.GONE
                binding.cardModeContainer.visibility = View.VISIBLE
            } else {
                //기본모드로 변경
                binding.followChangemode.setImageResource(R.drawable.follow_changemode)
                binding.followRv.visibility = View.VISIBLE
                binding.cardModeContainer.visibility = View.GONE
            }
        }

        var isnew = 0
        var isold = 0
        binding.followBtn1.setOnClickListener {
            if(isnew == 0 && isold == 0) {
                isnew = 1
                binding.followBtn1.setImageResource(R.drawable.follow_btn1_after)
            }
            else if (isold == 1) {
                isold = 0
                isnew = 1
                binding.followBtn2.setImageResource(R.drawable.follow_btn2)
                binding.followBtn1.setImageResource(R.drawable.follow_btn1_after)
            }
            else if (isnew == 1 && isold == 0) {
                isnew = 0
                binding.followBtn1.setImageResource(R.drawable.follow_btn1)
            }
            binding.followRv.adapter = combinedAdapter
        }
        binding.followBtn2.setOnClickListener {
            if(isnew == 0 && isold == 0) {
                isold = 1
                binding.followBtn2.setImageResource(R.drawable.follow_btn2_after)
            }
            else if (isnew == 1) {
                isnew = 0
                isold = 1
                binding.followBtn1.setImageResource(R.drawable.follow_btn1)
                binding.followBtn2.setImageResource(R.drawable.follow_btn2_after)
            }
            else if (isold == 1) {
                isold = 0
                binding.followBtn2.setImageResource(R.drawable.follow_btn2)
            }
            binding.followRv.adapter = combinedAdapter
        }


        //뒤로가기 클릭시
        binding.followBackbtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }

        //마이페이지 버튼 클릭시 -> 해당 파트가 없어서 잠시 주석처리 함
//        binding.followMypagebtn.setOnClickListener {
//            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_mypage
//        }

        //검색버튼 클릭시
        binding.followSearchbtn.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        return binding.root

    }

    // CardMode : viewpager2 안에 아이템 위치 설정
    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(0))
        transformer.addTransformer { page, position ->

            val absPos = abs(position)

            page.alpha = when {
                absPos >= 3 -> 0f
                absPos >= 2 -> 1f
                absPos >= 1 -> 1f
                else -> 1f
            }

            page.translationZ = when {
                absPos >= 3 -> 0f
                absPos >= 2 -> 1f
                absPos >= 1 -> 2f
                else -> 3f
            }

            page.scaleY = 0.85f + (1 - absPos) * 0.15f
            page.translationX = -position * (page.width / 2.5f)

        }

        binding.viewPager2.setPageTransformer(transformer)
    }

    // CardMode: 아이템 이동 시 위치 업로드 + 아이템 제목과 날짜 업로드
    private fun setPageChangeCallback() {
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("PageChange", "Selected page: $position")
                val item = items[position % items.size]
                binding.cardModeTitle.text = item.name
                binding.cardModeDate.text = item.date
            }
        })
    }

    // CardMode: 스크롤 방식 설정
    private fun setupViewPager(){
        // 더미 데이터
        items = listOf(
            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/%EC%8B%9C%EA%B0%84%EC%9D%98%20%EC%B4%88%EC%83%81.jpg", "『시간의 초상』展", "2022-07-30 ~ 2024.12.31"),
            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/%EC%A0%9C%209%ED%96%89%EC%84%B1.jpg", "SeMA 옴니버스 《제9행성》", "2024-07-31 ~ 2024-10.24"),
            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/%EC%84%9C%EB%8F%84%ED%98%B8jpg.jpg", "서도호: 스페큘레이션스", "2024-08-17 ~ 2024-11-03"),
            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/%E1%84%92%E1%85%A9%E1%84%90%E1%85%A6%E1%86%AF%E1%84%91%E1%85%A1%E1%84%85%E1%85%B5%E1%84%8E%E1%85%B5%E1%86%AF%20%E1%84%91%E1%85%A1%E1%86%B8%E1%84%8B%E1%85%A5%E1%86%B8%20in%20%E1%84%89%E1%85%A5%E1%84%8E%E1%85%A9%E1%86%AB.jpg", "호텔파리칠 팝업 in 서촌", "2024-08-22 ~ 2024-08-27"),
            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/%E1%84%80%E1%85%B5%E1%86%B7%E1%84%8C%E1%85%A5%E1%86%BC%E1%84%86%E1%85%AE%E1%86%AB%E1%84%8B%E1%85%A1%E1%86%AF%E1%84%85%E1%85%A9%E1%84%8B%E1%85%A6%20%E1%84%91%E1%85%A1%E1%86%B8%E1%84%8B%E1%85%A5%E1%86%B8%E1%84%89%E1%85%B3%E1%84%90%E1%85%A9%E1%84%8B%E1%85%A5%20%3C%E1%84%8F%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%A9%E1%86%BC%20%E1%84%8F%E1%85%B2%E1%84%8B%E1%85%A5%E1%84%85%E1%85%A2%E1%86%AB%E1%84%83%E1%85%B3%3E.jpg", "김정문알로에 팝업스토어 <크크롱 큐어랜드>", "2024-07-23 ~ 2024-09-01")
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 6", "2024.8.12 ~ 2024.8.12"),
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 7", "2024.8.12 ~ 2024.8.12"),
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 8", "2024.8.12 ~ 2024.8.12"),
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 9", "2024.8.12 ~ 2024.8.12"),
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 10", "2024.8.12 ~ 2024.8.12"),
//            CardItem("https://d1gkmwje8u679n.cloudfront.net/Image/giando.jpg", "기안도 11", "2024.8.12 ~ 2024.8.12")
        )

        // 아이템 클릭 시 액티비티로 데이터 전달
        viewPagerAdapter = FollowCardAdapter(requireContext(), items)

        binding.viewPager2.adapter = viewPagerAdapter
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewPager2.setCurrentItem(2, false)

        setUpTransformer()
        setPageChangeCallback()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedFilters.observe(viewLifecycleOwner) { filters ->
            filters.forEach { (region, districts) ->
                if (districts.isNotEmpty()) {
                    Log.d("HomeFragment", "Selected region: $region, districts: $districts")
                    loadFilterData(region, districts.toString())
                    isFiltered = true
                } else {
                    loadData()
                    isFiltered = false
                }
            }
        }

        // 하트의 상태 변화를 확인
        sharedViewModel.followedItems.observe(viewLifecycleOwner) {
            combinedAdapter.updateFilteredItems()
        }
    }

    private fun loadData() {
        if (isFiltered) return

        apiService.getTopLikedEx().enqueue(object : Callback<DataResponse<ExhibitionTopLiked>> {
            override fun onResponse(
                call: Call<DataResponse<ExhibitionTopLiked>>,
                response: Response<DataResponse<ExhibitionTopLiked>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let { list ->
                        val combinedResponses = list.map { data ->
                            CombinedResponse(
                                id = data.id,
                                name = data.name,
                                place = data.place,
                                description = data.description,
                                photoUrl = data.photoUrl,
                                startDate = data.startDate,
                                endDate = data.endDate,
                                heart = data.heart,
                                region = data.region,
                                district = data.district
                            )
                        }
                        addDataRecyclerView(combinedResponses)

                        val cardViewResponse = list.map { data ->
                            FollowCardItem(
                                id = data.id,
                                imageUrl = data.photoUrl,
                                name = data.name,
                                date = "${data.startDate}~${data.endDate}",
                                heart = data.heart
                            )
                        }
                        addDataViewpager(cardViewResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse<ExhibitionTopLiked>>, t: Throwable) {}
        })

        apiService.getLatestEx().enqueue(object : Callback<DataResponse<ExhibitionLatest>> {
            override fun onResponse(
                call: Call<DataResponse<ExhibitionLatest>>,
                response: Response<DataResponse<ExhibitionLatest>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let { list ->
                        val combinedResponses = list.map { data ->
                            CombinedResponse(
                                id = data.id,
                                name = data.name,
                                place = data.place,
                                description = data.description,
                                photoUrl = data.photoUrl,
                                startDate = data.startDate,
                                endDate = data.endDate,
                                heart = data.heart,
                                region = data.region,
                                district = data.district
                            )
                        }
                        addDataRecyclerView(combinedResponses)

                        val cardViewResponse = list.map { data ->
                            FollowCardItem(
                                id = data.id,
                                imageUrl = data.photoUrl,
                                name = data.name,
                                date = "${data.startDate}~${data.endDate}",
                                heart = data.heart
                            )
                        }
                        addDataViewpager(cardViewResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse<ExhibitionLatest>>, t: Throwable) {}
        })

        apiService.getTopLikedPopup().enqueue(object : Callback<DataResponse<PopupStoresTopLiked>> {
            override fun onResponse(
                call: Call<DataResponse<PopupStoresTopLiked>>,
                response: Response<DataResponse<PopupStoresTopLiked>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let { list ->
                        val combinedResponses = list.map { data ->
                            CombinedResponse(
                                id = data.id,
                                name = data.name,
                                place = data.place,
                                description = data.description,
                                photoUrl = data.photoUrl,
                                startDate = data.startDate,
                                endDate = data.endDate,
                                heart = data.heart,
                                region = data.region,
                                district = data.district
                            )
                        }
                        addDataRecyclerView(combinedResponses)

                        val cardViewResponse = list.map { data ->
                            FollowCardItem(
                                id = data.id,
                                imageUrl = data.photoUrl,
                                name = data.name,
                                date = "${data.startDate}~${data.endDate}",
                                heart = data.heart
                            )
                        }
                        addDataViewpager(cardViewResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponse<PopupStoresTopLiked>>, t: Throwable) {}
        })

        apiService.getLatestPopup().enqueue(object : Callback<DataResponse<PopupStoresLatest>> {
            override fun onResponse(
                call: Call<DataResponse<PopupStoresLatest>>,
                response: Response<DataResponse<PopupStoresLatest>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.content ?: emptyList()
                    val combinedResponses = list.map { data ->
                        CombinedResponse(
                            id = data.id,
                            name = data.name,
                            place = data.place,
                            description = data.description,
                            photoUrl = data.photoUrl,
                            startDate = data.startDate,
                            endDate = data.endDate,
                            heart = data.heart,
                            region = data.region,
                            district = data.district
                        )
                    }
                    addDataRecyclerView(combinedResponses)

                    val cardViewResponse = list.map { data ->
                        FollowCardItem(
                            id = data.id,
                            imageUrl = data.photoUrl,
                            name = data.name,
                            date = "${data.startDate}~${data.endDate}",
                            heart = data.heart
                        )
                    }
                    addDataViewpager(cardViewResponse)
                }
            }
            override fun onFailure(call: Call<DataResponse<PopupStoresLatest>>, t: Throwable) {}
        })

    }

    private fun loadFilterData(region: String, district: String) {
        apiService.getFilteredTopLikedEx(region, district).enqueue(object : Callback<FilterResponse<ExFilterTopLike>> {
            override fun onResponse(
                call: Call<FilterResponse<ExFilterTopLike>>,
                response: Response<FilterResponse<ExFilterTopLike>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.topLikedExhibitions ?: emptyList()
                    val combinedResponses = list.map { data ->
                        CombinedResponse(
                            id = data.id,
                            name = data.name,
                            place = data.place,
                            description = data.description,
                            photoUrl = data.photoUrl,
                            startDate = data.startDate,
                            endDate = data.endDate,
                            heart = data.heart,
                            region = data.region,
                            district = data.district
                        )
                    }
                    addDataRecyclerView(combinedResponses)

                    val cardViewResponse = list.map { data ->
                        FollowCardItem(
                            id = data.id,
                            imageUrl = data.photoUrl,
                            name = data.name,
                            date = "${data.startDate}~${data.endDate}",
                            heart = data.heart
                        )
                    }
                    addDataViewpager(cardViewResponse)
                }
            }
            override fun onFailure(call: Call<FilterResponse<ExFilterTopLike>>, t: Throwable) {}
        })

        apiService.getFilteredLatestEx(region, district).enqueue(object : Callback<FilterResponse<ExFilterLatest>> {
            override fun onResponse(
                call: Call<FilterResponse<ExFilterLatest>>,
                response: Response<FilterResponse<ExFilterLatest>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.latestExhibitions ?: emptyList()
                    val combinedResponses = list.map { data ->
                        CombinedResponse(
                            id = data.id,
                            name = data.name,
                            place = data.place,
                            description = data.description,
                            photoUrl = data.photoUrl,
                            startDate = data.startDate,
                            endDate = data.endDate,
                            heart = data.heart,
                            region = data.region,
                            district = data.district
                        )
                    }
                    addDataRecyclerView(combinedResponses)

                    val cardViewResponse = list.map { data ->
                        FollowCardItem(
                            id = data.id,
                            imageUrl = data.photoUrl,
                            name = data.name,
                            date = "${data.startDate}~${data.endDate}",
                            heart = data.heart
                        )
                    }
                    addDataViewpager(cardViewResponse)
                }
            }
            override fun onFailure(call: Call<FilterResponse<ExFilterLatest>>, t: Throwable) {}
        })

        apiService.getFilteredTopLikedPopup(region, district).enqueue(object : Callback<FilterResponse<PopupFilterTopLike>> {
            override fun onResponse(
                call: Call<FilterResponse<PopupFilterTopLike>>,
                response: Response<FilterResponse<PopupFilterTopLike>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.topLikedPopupStores ?: emptyList()
                    val combinedResponses = list.map { data ->
                        CombinedResponse(
                            id = data.id,
                            name = data.name,
                            place = data.place,
                            description = data.description,
                            photoUrl = data.photoUrl,
                            startDate = data.startDate,
                            endDate = data.endDate,
                            heart = data.heart,
                            region = data.region,
                            district = data.district
                        )
                    }
                    addDataRecyclerView(combinedResponses)

                    val cardViewResponse = list.map { data ->
                        FollowCardItem(
                            id = data.id,
                            imageUrl = data.photoUrl,
                            name = data.name,
                            date = "${data.startDate}~${data.endDate}",
                            heart = data.heart
                        )
                    }
                    addDataViewpager(cardViewResponse)
                }
            }
            override fun onFailure(call: Call<FilterResponse<PopupFilterTopLike>>, t: Throwable) {}
        })

        apiService.getFilteredLatestPopup(region, district).enqueue(object : Callback<FilterResponse<PopupFilterLatest>> {
            override fun onResponse(
                call: Call<FilterResponse<PopupFilterLatest>>,
                response: Response<FilterResponse<PopupFilterLatest>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.latestPopupStores ?: emptyList()
                    val combinedResponses = list.map { data ->
                        CombinedResponse(
                            id = data.id,
                            name = data.name,
                            place = data.place,
                            description = data.description,
                            photoUrl = data.photoUrl,
                            startDate = data.startDate,
                            endDate = data.endDate,
                            heart = data.heart,
                            region = data.region,
                            district = data.district
                        )
                    }
                    addDataRecyclerView(combinedResponses)

                    val cardViewResponse = list.map { data ->
                        FollowCardItem(
                            id = data.id,
                            imageUrl = data.photoUrl,
                            name = data.name,
                            date = "${data.startDate}~${data.endDate}",
                            heart = data.heart
                        )
                    }
                    addDataViewpager(cardViewResponse)
                }
            }
            override fun onFailure(call: Call<FilterResponse<PopupFilterLatest>>, t: Throwable) {}
        })
    }

    private fun addDataRecyclerView(newData: List<CombinedResponse>) {
        if (newData.isNotEmpty()) {
            allCombinedResponses.addAll(newData)
            combinedAdapter.updateData(allCombinedResponses)
        }
    }

    private fun addDataViewpager(newData: List<FollowCardItem>) {
        if (newData.isNotEmpty()) {
            allFollowCardItems.addAll(newData)
            //viewPagerAdapter.updateData(allFollowCardItems)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isFiltered", isFiltered)
        super.onSaveInstanceState(outState)
    }

}
