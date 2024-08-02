package com.example.moe

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.moe.databinding.ItemPageBinding
import com.example.moe.databinding.SearchedItemBinding
import kotlin.math.min


class PageRVAdapter(private val pageIndexList: List<Int>, private val viewModel: ViewModel): RecyclerView.Adapter<PageRVAdapter.ViewHolder>() {

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
                binding.pageTv.setTextColor(ContextCompat.getColor(binding.pageTv.context, R.color.white))
            } else{
                binding.pageBg.isSelected = false
                binding.pageTv.setTextColor(ContextCompat.getColor(binding.pageTv.context, R.color.black))
            }
        }
    }

}