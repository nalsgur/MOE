package com.example.moe.detail.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moe.detail.record.ui.DetailActivity
import com.example.moe.detail.search.remote.SearchViewModel
import com.example.moe.databinding.FragmentSearchResultBinding
import com.example.moe.detail.PageViewModel
import com.example.moe.detail.follow.remote.FollowService
import com.example.moe.detail.search.entities.Search

class SearchResultFragment(private val searchViewModel: SearchViewModel, private val pageViewModel: PageViewModel) : Fragment() {
    private lateinit var binding : FragmentSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getResult()
    }

    private fun getResult(){
        searchViewModel.searchState.observe(this){
            when{
                it.loading ->{
                    binding.searchPb.visibility = View.VISIBLE
                    binding.searchFailedTv.visibility = View.GONE
                    binding.paginationLl.visibility = View.GONE
                }
                it.error != null ->{
                    binding.searchPb.visibility = View.GONE
                    binding.searchFailedTv.visibility = View.VISIBLE
                    binding.paginationLl.visibility = View.GONE
                    Log.d("error", it.error)
                }
                else ->{
                    if (it.list != null) {
                        initRecyclerView(it.list)
                        setPageIndex(it.list)
                        binding.searchPb.visibility = View.GONE
                        binding.searchFailedTv.visibility = View.GONE
                        binding.paginationLl.visibility = View.VISIBLE
                    } else{
                        binding.searchPb.visibility = View.GONE
                        binding.searchFailedTv.visibility = View.VISIBLE
                        binding.paginationLl.visibility = View.GONE
                        Log.d("result", "no result")
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(list: List<Search>) {
        val searchAdapter = SearchRVAdapter(list, pageViewModel)
        binding.searchResultRv.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.searchResultRv.adapter = searchAdapter

        pageViewModel.pageIndex.observe(this) {
            searchAdapter.notifyDataSetChanged()
        }

        searchAdapter.setMyItemClickListener(object : SearchRVAdapter.MyItemClickListener {
            override fun onClickItem(search: Search) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("search", search)
                startActivity(intent)
            }

            override fun onClickHeart(search: Search) {
                searchViewModel.setLike(2)
                search.follow = !search.follow
                searchAdapter.notifyDataSetChanged()
                val followService = FollowService()
                if(search.follow){
                    if(search.popupStore){
                        followService.followPopupStore(1, search.id)
                    } else{
                        followService.followExhibition(1, search.id)
                    }
                } else{
                    followService.unfollow(search.id)
                }
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPageIndex(list: List<Search>){
        val resultCount = list.size
        val totalPages = if (resultCount % 6 == 0) resultCount / 6 else resultCount / 6 + 1
        val pageIndexList = splitIntoChunks(totalPages)
        val pageAdapter = PageRVAdapter(pageIndexList, pageViewModel)
        binding.pageRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.pageRv.adapter = pageAdapter

        binding.pageNextBtn.setOnClickListener {
            if(pageViewModel.pageIndex.value!! < totalPages){
                if (pageViewModel.pageIndex.value!! % 3 == 0){
                    pageViewModel.increaseDisplayIndex()
                    pageViewModel.increaseOffset()
                    pageViewModel.increaseIndex()
                }else{
                    pageViewModel.increaseIndex()
                }
            }
        }

        binding.pagePrevBtn.setOnClickListener {
            if(pageViewModel.pageIndex.value!! > 1){
                if (pageViewModel.pageIndex.value!! % 3 == 1){
                    pageViewModel.decreaseDisplayIndex()
                    pageViewModel.decreaseOffset()
                    pageViewModel.decreaseIndex()
                }else{
                    pageViewModel.decreaseIndex()
                }
            }
        }

        pageViewModel.pageIndex.observe(this) {
            pageAdapter.notifyDataSetChanged()
        }
        pageViewModel.displayIndex.observe(this){
            pageAdapter.notifyDataSetChanged()
        }
        pageViewModel.offset.observe(this){
            pageAdapter.notifyDataSetChanged()
        }
    }

    private fun splitIntoChunks(number: Int): List<Int> {
        val result = mutableListOf<Int>()
        var remaining = number

        while (remaining > 0) {
            if (remaining >= 3) {
                result.add(3)
            } else {
                result.add(remaining)
            }
            remaining -= 3
        }

        return result
    }
}