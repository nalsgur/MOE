package com.example.moe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moe.MainAPI.DataResponse
import com.example.moe.MainAPI.ExFilterLatest
import com.example.moe.MainAPI.ExFilterTopLike
import com.example.moe.databinding.FragmentHomeBinding
import com.example.moe.MainAPI.ExhibitionLatest
import com.example.moe.MainAPI.ExLatestAdapter
import com.example.moe.MainAPI.ExhibitionTopLiked
import com.example.moe.MainAPI.ExTopLikedAdapter
import com.example.moe.MainAPI.FilterResponse
import com.example.moe.MainAPI.PopupFilterLatest
import com.example.moe.MainAPI.PopupFilterTopLike
import com.example.moe.MainAPI.PopupLatestAdapter
import com.example.moe.MainAPI.PopupTopLikedAdapter
import com.example.moe.MainAPI.PopupStoresLatest
import com.example.moe.MainAPI.PopupStoresTopLiked
import com.example.moe.MainAPI.RetrofitClient
import com.example.moe.MainAPI.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var exAdapterTopLiked: ExTopLikedAdapter
    private lateinit var exAdapterLatest: ExLatestAdapter
    private lateinit var popupAdapterTopLiked: PopupTopLikedAdapter
    private lateinit var popupAdapterLatest: PopupLatestAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private var isLoadingViewRemoved = false

    private val apiService by lazy { RetrofitClient.HomeApiService() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()

        loadData(isTopLiked = true)
        loadData(isTopLiked = false)

        if (savedInstanceState != null) {
            isLoadingViewRemoved = savedInstanceState.getBoolean("LoadingViewRemoved", false)
        }

        if (isLoadingViewRemoved) {
            binding.homeLoadingCt.visibility = View.GONE
        } else {
            binding.homeLoadingCt.visibility = View.VISIBLE
        }

        Handler().postDelayed({
            if (!isLoadingViewRemoved) {
                binding.homeLoadingCt.visibility = View.GONE
                isLoadingViewRemoved = true
            }
        }, 2000)

        binding.homeShowBtn.setOnClickListener{
            showRecyclerViews()
            binding.homeShowBtn.isSelected = true
            binding.homeStoreBtn.isSelected = false
        }

        binding.homeStoreBtn.setOnClickListener {
            storeRecyclerViews()
            binding.homeStoreBtn.isSelected = true
            binding.homeShowBtn.isSelected = false
        }

        binding.homeSearch.setOnClickListener{
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("LoadingViewRemoved", isLoadingViewRemoved)
    }

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

    private fun setupRecyclerView() {
        exAdapterTopLiked = ExTopLikedAdapter(isTopLiked = true)
        exAdapterLatest = ExLatestAdapter(isTopLiked = true)
        popupAdapterTopLiked = PopupTopLikedAdapter(isTopLiked = true)
        popupAdapterLatest = PopupLatestAdapter(isTopLiked = true)

        binding.homeRcShow2.adapter = exAdapterTopLiked
        binding.homeRcShow1.adapter = exAdapterLatest
        binding.homeRcPopUp2.adapter = popupAdapterTopLiked
        binding.homeRcPopUp1.adapter = popupAdapterLatest

        exAdapterTopLiked.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        exAdapterLatest.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        popupAdapterTopLiked.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        popupAdapterLatest.onItemClickListener = { search ->
            navigateToDetail(search)
        }
    }

    private fun navigateToDetail(search: Search) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("search", search)
        startActivity(intent)
    }

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


    private fun showRecyclerViews() {
        binding.homeShowBtn.setTextColor(Color.WHITE)
        binding.homeStoreBtn.setTextColor(Color.BLACK)

        binding.homeSubTlShow1.visibility = View.VISIBLE
        binding.homeRcShow1.visibility = View.VISIBLE
        binding.homeSubTlShow2.visibility = View.VISIBLE
        binding.homeRcShow2.visibility = View.VISIBLE

        binding.homeSubTlPopUp1.visibility = View.GONE
        binding.homeRcPopUp1.visibility = View.GONE
        binding.homeSubTlPopUp2.visibility = View.GONE
        binding.homeRcPopUp2.visibility = View.GONE
    }

    private fun storeRecyclerViews() {
        binding.homeStoreBtn.setTextColor(Color.WHITE)
        binding.homeShowBtn.setTextColor(Color.BLACK)

        binding.homeSubTlPopUp1.visibility = View.VISIBLE
        binding.homeRcPopUp1.visibility = View.VISIBLE
        binding.homeSubTlPopUp2.visibility = View.VISIBLE
        binding.homeRcPopUp2.visibility = View.VISIBLE

        binding.homeSubTlShow1.visibility = View.GONE
        binding.homeRcShow1.visibility = View.GONE
        binding.homeSubTlShow2.visibility = View.GONE
        binding.homeRcShow2.visibility = View.GONE
        binding.subSpace.visibility = View.GONE

    }

}