package com.example.moe

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
import com.example.moe.databinding.FragmentSearchResultBinding

class SearchResultFragment(private val viewModel: SearchViewModel) : Fragment() {
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
        viewModel.searchState.observe(this){
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
        val searchAdapter = SearchRVAdapter(list, viewModel)
        binding.searchResultRv.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.searchResultRv.adapter = searchAdapter

        viewModel.pageIndex.observe(this) {
            searchAdapter.notifyDataSetChanged()
        }

        searchAdapter.setMyItemClickListener(object : SearchRVAdapter.MyItemClickListener{
            override fun onClickItem(search: Search) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("search", search)
                startActivity(intent)
            }

//            override fun onClickHeart(search: Search) {
//
//            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPageIndex(list: List<Search>){
        val resultCount = list.size
        val totalPages = if (resultCount % 6 == 0) resultCount / 6 else resultCount / 6 + 1
        val pageIndexList = splitIntoChunks(totalPages)
        val pageAdapter = PageRVAdapter(pageIndexList, viewModel)
        binding.pageRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.pageRv.adapter = pageAdapter

        binding.pageNextBtn.setOnClickListener {
            if(viewModel.pageIndex.value!! < totalPages){
                if (viewModel.pageIndex.value!! % 3 == 0){
                    viewModel.increaseDisplayIndex()
                    viewModel.increaseOffset()
                    viewModel.increaseIndex()
                }else{
                    viewModel.increaseIndex()
                }
            }
        }

        binding.pagePrevBtn.setOnClickListener {
            if(viewModel.pageIndex.value!! > 1){
                if (viewModel.pageIndex.value!! % 3 == 1){
                    viewModel.decreaseDisplayIndex()
                    viewModel.decreaseOffset()
                    viewModel.decreaseIndex()
                }else{
                    viewModel.decreaseIndex()
                }
            }
        }

        viewModel.pageIndex.observe(this) {
            pageAdapter.notifyDataSetChanged()
        }
        viewModel.displayIndex.observe(this){
            pageAdapter.notifyDataSetChanged()
        }
        viewModel.offset.observe(this){
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