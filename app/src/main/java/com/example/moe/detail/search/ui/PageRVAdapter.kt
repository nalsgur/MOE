package com.example.moe.detail.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moe.R
import com.example.moe.databinding.ItemPageBinding
import com.example.moe.detail.PageViewModel


class PageRVAdapter(private val pageIndexList: List<Int>, private val viewModel: PageViewModel): RecyclerView.Adapter<PageRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPageBinding = ItemPageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position + 1 + viewModel.offset.value!!)
        holder.binding.index.setOnClickListener {
            viewModel.updateIndex(position+1+viewModel.offset.value!!)
        }
    }

    override fun getItemCount(): Int = pageIndexList[viewModel.displayIndex.value!!]


    inner class ViewHolder(val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.pageTv.text = position.toString()
            if(position == viewModel.pageIndex.value){
                binding.pageBg.isSelected = true
                binding.pageTv.setTextColor(ContextCompat.getColor(binding.pageTv.context,
                    R.color.white
                ))
            } else{
                binding.pageBg.isSelected = false
                binding.pageTv.setTextColor(ContextCompat.getColor(binding.pageTv.context,
                    R.color.black
                ))
            }
        }
    }

}