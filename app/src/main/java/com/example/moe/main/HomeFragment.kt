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
import com.example.moe.MainAPI.CombinedAdapter
import com.example.moe.MainAPI.DataResponse
import com.example.moe.MainAPI.ExFilterLatest
import com.example.moe.MainAPI.ExFilterTopLike
import com.example.moe.databinding.FragmentHomeBinding
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
import com.example.moe.detail.record.ui.DetailActivity
import com.example.moe.detail.search.entities.Search
import com.example.moe.detail.search.ui.SearchActivity
import com.example.moe.main.MainAPI.ExFilterLatestAdapter
import com.example.moe.main.MainAPI.ExFilterTopLikedAdapter
import com.example.moe.main.MainAPI.PopupFilterLatestAdapter
import com.example.moe.main.MainAPI.PopupFilterTopLikedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedViewModel: SharedViewModel
    private var isLoadingViewRemoved = false
    private var isFiltered = false

    private lateinit var exAdapterTopLiked: ExTopLikedAdapter
    private lateinit var exAdapterLatest: ExLatestAdapter
    private lateinit var popupAdapterTopLiked: PopupTopLikedAdapter
    private lateinit var popupAdapterLatest: PopupLatestAdapter

    private lateinit var exFilterAdapterTopLiked: ExFilterTopLikedAdapter
    private lateinit var exFilterAdapterLatest: ExFilterLatestAdapter
    private lateinit var popupFilterAdapterTopLiked: PopupFilterTopLikedAdapter
    private lateinit var popupFilterAdapterLatest: PopupFilterLatestAdapter

    private val apiService by lazy { RetrofitClient.HomeApiService() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        setupDefaultRecyclerView()
        setupFilterRecyclerView()

        loadData()

        if (savedInstanceState != null) {
            isLoadingViewRemoved = savedInstanceState.getBoolean("LoadingViewRemoved", false)
            isFiltered = savedInstanceState.getBoolean("isFiltered", false)
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
        outState.putBoolean("isFiltered", isFiltered)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedFilters.observe(viewLifecycleOwner) {filters ->
            filters.forEach{(region, districts) ->
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

        sharedViewModel.followedItems.observe(viewLifecycleOwner) {
            exAdapterTopLiked.notifyDataSetChanged()
            exAdapterLatest.notifyDataSetChanged()
            popupAdapterTopLiked.notifyDataSetChanged()
            popupAdapterLatest.notifyDataSetChanged()

            exFilterAdapterTopLiked.notifyDataSetChanged()
            exFilterAdapterLatest.notifyDataSetChanged()
            popupFilterAdapterTopLiked.notifyDataSetChanged()
            popupFilterAdapterLatest.notifyDataSetChanged()
        }

    }

    private fun setupDefaultRecyclerView() {
        exAdapterTopLiked = ExTopLikedAdapter(sharedViewModel = sharedViewModel)
        exAdapterLatest = ExLatestAdapter(sharedViewModel = sharedViewModel)
        popupAdapterTopLiked = PopupTopLikedAdapter(sharedViewModel = sharedViewModel)
        popupAdapterLatest = PopupLatestAdapter(sharedViewModel = sharedViewModel)

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

    private fun setupFilterRecyclerView() {
        exFilterAdapterTopLiked = ExFilterTopLikedAdapter(sharedViewModel = sharedViewModel)
        exFilterAdapterLatest = ExFilterLatestAdapter(sharedViewModel = sharedViewModel)
        popupFilterAdapterTopLiked = PopupFilterTopLikedAdapter(sharedViewModel = sharedViewModel)
        popupFilterAdapterLatest = PopupFilterLatestAdapter(sharedViewModel = sharedViewModel)

        binding.homeRcShow2.adapter = exFilterAdapterTopLiked
        binding.homeRcShow1.adapter = exFilterAdapterLatest
        binding.homeRcPopUp2.adapter = popupFilterAdapterTopLiked
        binding.homeRcPopUp1.adapter = popupFilterAdapterLatest

        exFilterAdapterTopLiked.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        exFilterAdapterLatest.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        popupFilterAdapterTopLiked.onItemClickListener = { search ->
            navigateToDetail(search)
        }

        popupFilterAdapterLatest.onItemClickListener = { search ->
            navigateToDetail(search)
        }
    }

    private fun navigateToDetail(search: Search) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("search", search)
        startActivity(intent)
    }

    private fun loadData() {
        if (isFiltered) return

        apiService.getTopLikedEx().enqueue(object : Callback<DataResponse<ExhibitionTopLiked>> {
            override fun onResponse(
                call: Call<DataResponse<ExhibitionTopLiked>>,
                response: Response<DataResponse<ExhibitionTopLiked>>
            ) {
                if (response.isSuccessful) {
                    val exTopLiked = response.body()?.content ?: emptyList()
                    exAdapterTopLiked.updateDefaultData(exTopLiked)
                    binding.homeRcShow2.adapter = exAdapterTopLiked
                    Log.d("HomeFragment", "Fetched top liked exhibitions: $exTopLiked")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DataResponse<ExhibitionTopLiked>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch top liked exhibitions", t)
            }
        })

        apiService.getLatestEx().enqueue(object : Callback<DataResponse<ExhibitionLatest>> {
            override fun onResponse(
                call: Call<DataResponse<ExhibitionLatest>>,
                response: Response<DataResponse<ExhibitionLatest>>
            ) {
                if (response.isSuccessful) {
                    val exLatest = response.body()?.content ?: emptyList()
                    exAdapterLatest.updateDefaultData(exLatest)
                    binding.homeRcShow1.adapter = exAdapterLatest
                    Log.d("HomeFragment", "Fetched latest exhibitions: $exLatest")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DataResponse<ExhibitionLatest>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch latest exhibitions", t)
            }
        })

        apiService.getTopLikedPopup().enqueue(object : Callback<DataResponse<PopupStoresTopLiked>> {
            override fun onResponse(
                call: Call<DataResponse<PopupStoresTopLiked>>,
                response: Response<DataResponse<PopupStoresTopLiked>>
            ) {
                if (response.isSuccessful) {
                    val popupTopLiked = response.body()?.content ?: emptyList()
                    popupAdapterTopLiked.updateDefaultData(popupTopLiked)
                    binding.homeRcPopUp2.adapter = popupAdapterTopLiked
                    Log.d("HomeFragment", "Fetched top liked popup stores: $popupTopLiked")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DataResponse<PopupStoresTopLiked>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch top liked popup store", t)
            }
        })

        apiService.getLatestPopup().enqueue(object : Callback<DataResponse<PopupStoresLatest>> {
            override fun onResponse(
                call: Call<DataResponse<PopupStoresLatest>>,
                response: Response<DataResponse<PopupStoresLatest>>
            ) {
                if (response.isSuccessful) {
                    val popupLatest = response.body()?.content ?: emptyList()
                    popupAdapterLatest.updateDefaultData(popupLatest)
                    binding.homeRcPopUp1.adapter = popupAdapterLatest
                    Log.d("HomeFragment", "Fetched latest popup stores: $popupLatest")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<DataResponse<PopupStoresLatest>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch latest popup store", t)
            }
        })

    }

    private fun loadFilterData(region: String, district: String) {
        apiService.getFilteredTopLikedEx(region, district).enqueue(object : Callback<FilterResponse<ExFilterTopLike>> {
            override fun onResponse(
                call: Call<FilterResponse<ExFilterTopLike>>,
                response: Response<FilterResponse<ExFilterTopLike>>
            ) {
                if (response.isSuccessful) {
                    val exFilterTopLiked = response.body()?.topLikedExhibitions ?: emptyList()
                    exFilterAdapterTopLiked.updateFilterData(exFilterTopLiked)
                    binding.homeRcShow2.adapter = exFilterAdapterTopLiked
                    Log.d("HomeFragment", "Fetched filter top liked exhibitions: $exFilterTopLiked")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FilterResponse<ExFilterTopLike>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch filter top liked exhibitions", t)
            }
        })

        apiService.getFilteredLatestEx(region, district).enqueue(object : Callback<FilterResponse<ExFilterLatest>> {
            override fun onResponse(
                call: Call<FilterResponse<ExFilterLatest>>,
                response: Response<FilterResponse<ExFilterLatest>>
            ) {
                if (response.isSuccessful) {
                    val exFilterLatest = response.body()?.latestExhibitions ?: emptyList()
                    exFilterAdapterLatest.updateFilterData(exFilterLatest)
                    binding.homeRcShow1.adapter = exFilterAdapterLatest
                    Log.d("HomeFragment", "Fetched filter latest exhibitions: $exFilterLatest")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FilterResponse<ExFilterLatest>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch filter latest exhibitions", t)
            }
        })

        apiService.getFilteredTopLikedPopup(region, district).enqueue(object : Callback<FilterResponse<PopupFilterTopLike>> {
            override fun onResponse(
                call: Call<FilterResponse<PopupFilterTopLike>>,
                response: Response<FilterResponse<PopupFilterTopLike>>
            ) {
                if (response.isSuccessful) {
                    val popupFilterTopLiked = response.body()?.topLikedPopupStores ?: emptyList()
                    popupFilterAdapterTopLiked.updateFilterData(popupFilterTopLiked)
                    binding.homeRcPopUp2.adapter = popupFilterAdapterTopLiked
                    Log.d("HomeFragment", "Fetched filter top liked popup stores: $popupFilterTopLiked")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FilterResponse<PopupFilterTopLike>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch filter top liked popup stores", t)
            }
        })

        apiService.getFilteredLatestPopup(region, district).enqueue(object : Callback<FilterResponse<PopupFilterLatest>> {
            override fun onResponse(
                call: Call<FilterResponse<PopupFilterLatest>>,
                response: Response<FilterResponse<PopupFilterLatest>>
            ) {
                if (response.isSuccessful) {
                    val popupFilterLatest = response.body()?.latestPopupStores ?: emptyList()
                    popupFilterAdapterLatest.updateFilterData(popupFilterLatest)
                    binding.homeRcPopUp1.adapter = popupFilterAdapterLatest
                    Log.d("HomeFragment", "Fetched filter latest popup stores: $popupFilterLatest")
                } else {
                    Log.e("HomeFragment", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FilterResponse<PopupFilterLatest>>, t: Throwable) {
                Log.e("HomeFragment", "Failed to fetch filter latest popup stores", t)
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