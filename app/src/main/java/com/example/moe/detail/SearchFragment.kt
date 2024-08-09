package com.example.moe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moe.databinding.FragmentSearchBinding

class SearchFragment(private val viewModel: SearchViewModel) : Fragment() {
    private lateinit var binding : FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getRecentSearchData()
    }

    private fun getRecentSearchData(){
        viewModel.recentSearchState.observe(this){
            when{
                it.loading ->{
                    binding.searchPb.visibility = View.VISIBLE
                    binding.noResultTv.visibility = View.GONE
                    binding.searchRv.visibility = View.GONE
                }
                it.error != null ->{
                    binding.searchPb.visibility = View.GONE
                    binding.noResultTv.visibility = View.VISIBLE
                    binding.searchRv.visibility = View.GONE
                }
                else ->{
                    if (it.list != null) {
                        initRecyclerView(it.list)
                        binding.searchPb.visibility = View.GONE
                        binding.noResultTv.visibility = View.GONE
                        binding.searchRv.visibility = View.VISIBLE
                    } else{
                        binding.searchPb.visibility = View.GONE
                        binding.noResultTv.visibility = View.VISIBLE
                        binding.searchRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(list: List<Search>) {
        val adapter = RecentSearchRVAdapter(list, viewModel)
        binding.searchRv.layoutManager = GridLayoutManager(context, 2,  GridLayoutManager.VERTICAL, false)
        binding.searchRv.adapter = adapter
        viewModel.recentSearchState.observe(this){
            adapter.notifyDataSetChanged()
        }

        adapter.setMyItemClickListener(object : RecentSearchRVAdapter.MyItemClickListener{
            override fun onClickItem(search: Search) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("search", search)
                startActivity(intent)
            }
        })
    }
}