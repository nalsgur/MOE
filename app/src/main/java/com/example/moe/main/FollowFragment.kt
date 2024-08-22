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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.moe.MainActivity
import com.example.moe.R
import com.example.moe.databinding.FragmentFollowBinding
import com.example.moe.databinding.ItemFollowBinding
import com.example.moe.detail.search.entities.Search
import com.example.moe.detail.search.remote.SearchViewModel
import com.example.moe.detail.search.ui.PageRVAdapter
import com.example.moe.detail.search.ui.SearchActivity
import com.example.moe.detail.search.ui.SearchRVAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment() : Fragment() {
    private lateinit var binding : FragmentFollowBinding
    private val apiService by lazy { RetrofitClient.HomeApiService() }
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var exAdapterLatest: ExLatestAdapter
    private lateinit var exAdapterTopLiked: ExTopLikedAdapter
    private lateinit var popupAdapterTopLiked: PopupTopLikedAdapter
    private lateinit var popupAdapterLatest: PopupLatestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater,container,false)

        val recyclerview = binding.followRv
65
        setupRecyclerView() //수진님 코드

        loadData(isTopLiked = true) //수진님 코드

//        val itemlist = ArrayList<FollowItem>()
//        itemlist.add(FollowItem("기안도1","12.13"))
//        itemlist.add(FollowItem("기안도2","12.13"))
//        itemlist.add(FollowItem("기안도3","12.13"))
//        itemlist.add(FollowItem("기안도4","12.13"))
//
//        val followadapter = FollowRecyclerAdapter(itemlist)
//        followadapter.notifyDataSetChanged()


        recyclerview.adapter = exAdapterLatest
        recyclerview.layoutManager = GridLayoutManager(context,2)

        binding.followChangemode.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_frm, FollowCardFragment()).commit()
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
            binding.followRv.adapter = exAdapterTopLiked
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
            binding.followRv.adapter = exAdapterLatest
        }
        

        //뒤로가기 클릭시
        binding.followBackbtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }

        //마이페이지 버튼 클릭시
        binding.followMypagebtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_mypage
        }

        //검색버튼 클릭시
        binding.followSearchbtn.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }


        return binding.root

    }


    //수진님 코드
    private fun setupRecyclerView() {
        exAdapterTopLiked = ExTopLikedAdapter(isTopLiked = true)
        exAdapterLatest = ExLatestAdapter(isTopLiked = true)
        popupAdapterTopLiked = PopupTopLikedAdapter(isTopLiked = true)
        popupAdapterLatest = PopupLatestAdapter(isTopLiked = true)
    }

    //수진님 코드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.selectedFilters.observe(viewLifecycleOwner) {filters ->
            filters.forEach{(region, districts) ->
                if (districts.isNotEmpty()) {
                    loadData(region, districts[0], true)
                } else {
                    loadData(isTopLiked = true)
                    loadData(isTopLiked = false)
                }
            }
        }
    }

    //수진님 코드
    private fun loadData(region: String? = null, district: String? =null, isTopLiked: Boolean = true) {
        val exTopLikedCall: Call<Any> = when {
            region != null && district != null && !isTopLiked -> {
                apiService.getFilteredTopLikedEx(region, district) as Call<Any>
            }
            else -> {
                apiService.getTopLikedEx() as Call<Any>
            }
        }

        exTopLikedCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data is DataResponse<*>) {
                        val exTopLiked = (data as DataResponse<ExhibitionTopLiked>).content
                        Log.d("HomeFragment", "Fetched top liked exhibitions: $exTopLiked")
                        exAdapterTopLiked.updateDefaultData(exTopLiked)
                    } else if (data is FilterResponse<*>) {
                        val exTopLiked = (data as FilterResponse<ExFilterTopLike>).content
                        Log.d("HomeFragment", "Fetched top liked filter exhibitions: $exTopLiked")
                        exAdapterTopLiked.updateFilterData(exTopLiked)
                    }

                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch top liked exhibitions", t)
            }
        })


        val exLatestCall: Call<Any> = when {
            region != null && district != null && !isTopLiked -> {
                apiService.getFilteredLatestEx(region, district) as Call<Any>
            }
            else -> {
                apiService.getLatestEx() as Call<Any>
            }
        }

        exLatestCall.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data is DataResponse<*>) {
                        val exLatest = (data as DataResponse<ExhibitionLatest>).content
                        Log.d("HomeFragment", "Fetched latest exhibitions: $exLatest")
                        exAdapterLatest.updateDefaultData(exLatest)
                    } else if (data is FilterResponse<*>) {
                        val exLatest = (data as FilterResponse<ExFilterLatest>).content
                        Log.d("HomeFragment", "Fetched latest filter exhibitions: $exLatest")
                        exAdapterLatest.updateFilterData(exLatest)
                    }
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch latest exhibitions", t)
            }
        })


        val popupTopLikeCall: Call<Any> = when {
            region != null && district != null && !isTopLiked -> {
                apiService.getFilteredTopLikedPopup(region, district) as Call<Any>
            }
            else -> {
                apiService.getTopLikedPopup() as Call<Any>
            }
        }

        popupTopLikeCall.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data is DataResponse<*>) {
                        val popupTopLike = (data as DataResponse<PopupStoresTopLiked>).content
                        Log.d("HomeFragment", "Fetched top liked popup store: $popupTopLike")
                        popupAdapterTopLiked.updateDefaultData(popupTopLike)
                    } else if (data is FilterResponse<*>) {
                        val popupTopLike = (data as FilterResponse<PopupFilterTopLike>).content
                        Log.d("HomeFragment", "Fetched top liked filter popup store: $popupTopLike")
                        popupAdapterTopLiked.updateFilterData(popupTopLike)
                    }
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch top liked popup store", t)
            }
        })


        val popupLatestCall: Call<Any> = when {
            region != null && district != null && !isTopLiked -> {
                apiService.getFilteredLatestPopup(region, district) as Call<Any>
            }
            else -> {
                apiService.getLatestPopup() as Call<Any>
            }
        }

        popupLatestCall.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data is DataResponse<*>) {
                        val popupLatest = (data as DataResponse<PopupStoresLatest>).content
                        Log.d("HomeFragment", "Fetched latest popup store: $popupLatest")
                        popupAdapterLatest.updateDefaultData(popupLatest)
                    } else if (data is FilterResponse<*>) {
                        val popupLatest = (data as FilterResponse<PopupFilterLatest>).content
                        Log.d("HomeFragment", "Fetched latest filter popup store: $popupLatest")
                        popupAdapterLatest.updateFilterData(popupLatest)
                    }
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch latest popup store", t)
            }
        })
    }


}

class FollowRecyclerAdapter (
    var FollowList : MutableList<FollowItem>,
) : RecyclerView.Adapter<FollowRecyclerAdapter.FollowViewHolder>() {

    inner class FollowViewHolder(val binding : ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        val image : ImageView
        val title : TextView
        val heart : ImageView
        val date : TextView

        init {
            image = itemView.findViewById(R.id.follow_item_image)
            title = itemView.findViewById(R.id.follow_item_title)
            heart = itemView.findViewById(R.id.follow_item_heart)
            date = itemView.findViewById(R.id.follow_item_date)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow,parent, false)
        return FollowViewHolder(ItemFollowBinding.bind(view))
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {

        holder.title.text = FollowList.get(position).title

        holder.date.text = FollowList.get(position).date

        var isheart = false
        holder.binding.followItemHeart.setOnClickListener {
            isheart = !isheart
            if(isheart) {
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart)
            } else {
                holder.binding.followItemHeart.setImageResource(R.drawable.follow_heart_after)
            }
        }
    }

    override fun getItemCount(): Int {
        return FollowList.size
    }

}

class FollowItem (val title : String, val date : String)
