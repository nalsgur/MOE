package com.example.moe.detail.record.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moe.R
import com.example.moe.databinding.FragmentRecordBinding
import com.example.moe.detail.PageViewModel
import com.example.moe.detail.record.entities.Record
import com.example.moe.detail.record.remote.RecordViewModel
import com.example.moe.detail.search.entities.Search
import com.example.moe.detail.search.ui.PageRVAdapter
import com.example.moe.detail.search.ui.SearchActivity
import com.example.moe.main.MainActivity

class RecordFragment(private val recordViewModel: RecordViewModel, private val pageViewModel: PageViewModel) : Fragment() {
    private lateinit var binding : FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(layoutInflater)
        recordViewModel.getRecordLatest(1, 1)
        binding.latest.isSelected = true
        binding.oldest.isSelected = false

        binding.backBtn.setOnClickListener {
            (activity as MainActivity).binding.mainBtv.selectedItemId = R.id.fragment_home
        }
        binding.searchIcon.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
        recordViewModel.recordState.observe(this){
            when{
                it.loading ->{
                    binding.recordPb.visibility = View.VISIBLE
                    binding.noResultTv.visibility = View.GONE
                    binding.recordRv.visibility = View.GONE
                    binding.paginationLl.visibility = View.GONE
                    binding.oldest.visibility = View.GONE
                    binding.latest.visibility = View.GONE
                    binding.phraseTv.visibility = View.GONE
                }
                it.error != null ->{
                    binding.recordPb.visibility = View.GONE
                    binding.noResultTv.visibility = View.VISIBLE
                    binding.recordRv.visibility = View.GONE
                    binding.paginationLl.visibility = View.GONE
                    binding.oldest.visibility = View.GONE
                    binding.latest.visibility = View.GONE
                    binding.phraseTv.visibility = View.GONE
                }
                else ->{
                    if (it.response!!.isNotEmpty()) {
                        initRecyclerView(it.response)
                        setPageIndex(it.response)
                        binding.recordPb.visibility = View.GONE
                        binding.noResultTv.visibility = View.GONE
                        binding.recordRv.visibility = View.VISIBLE
                        binding.paginationLl.visibility = View.VISIBLE
                        binding.oldest.visibility = View.VISIBLE
                        binding.latest.visibility = View.VISIBLE
                        binding.phraseTv.visibility = View.VISIBLE
                        Log.d("record", it.response.toString())
                    } else{
                        binding.recordPb.visibility = View.GONE
                        binding.noResultTv.visibility = View.VISIBLE
                        binding.recordRv.visibility = View.GONE
                        binding.paginationLl.visibility = View.GONE
                        binding.oldest.visibility = View.GONE
                        binding.latest.visibility = View.GONE
                        binding.phraseTv.visibility = View.GONE
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(record: List<Record>) {
        val recordAdapter = RecordRVAdapter(record, pageViewModel)
        binding.recordRv.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recordRv.adapter = recordAdapter

        pageViewModel.pageIndex.observe(this) {
            recordAdapter.notifyDataSetChanged()
        }

        recordAdapter.setMyItemClickListener(object : RecordRVAdapter.MyItemClickListener {
            override fun onClickItem(record: Record) {
                val intent = Intent(context, DetailActivity::class.java)
                val search = Search(
                    record.recordPageId,
                    record.name,
                    record.photo,
                    record.startDate,
                    record.endDate,
                    record.heart,
                    true
                )
                intent.putExtra("search", search)
                startActivity(intent)
            }

            override fun onClickHeart(record: Record) {
                TODO("Not yet implemented")
            }

        })

        binding.latest.setOnClickListener {
            recordViewModel.getRecordLatest(1, 1)
            binding.oldest.isSelected = false
            binding.oldest.setTextColor(
                ContextCompat.getColor(requireContext(),
                    R.color.navy
                ))
            binding.latest.isSelected = true
            binding.latest.setTextColor(
                ContextCompat.getColor(requireContext(),
                    R.color.white
                ))
        }

        binding.oldest.setOnClickListener {
            recordViewModel.getRecordOldest(1, 1)
            binding.oldest.isSelected = true
            binding.oldest.setTextColor(
                ContextCompat.getColor(requireContext(),
                R.color.white
            ))
            binding.latest.isSelected = false
            binding.latest.setTextColor(
                ContextCompat.getColor(requireContext(),
                    R.color.navy
                ))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPageIndex(list: List<Record>){
        val resultCount = list.size
        val totalPages = if (resultCount % 4 == 0) resultCount / 4 else resultCount / 4 + 1
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