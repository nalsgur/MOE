package com.example.moe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.databinding.SearchedItemBinding


class RecentSearchRVAdapter (private val search: List<Search>, private val viewModel: SearchViewModel): RecyclerView.Adapter<RecentSearchRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onClickItem(search: Search)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: SearchedItemBinding = SearchedItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(search[position])
        holder.binding.searchItemCl.setOnClickListener {
            mItemClickListener.onClickItem(search[position])
        }
    }

    override fun getItemCount(): Int = search.size


    inner class ViewHolder(val binding: SearchedItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(search: Search){
            Glide.with(binding.image.context)
                .load(search.photo)
                .into(binding.image)
            binding.itemTitle.text = search.title
            val date = search.startDate.substring(0,10).replace("-", ".") + " ~ " + search.endDate.substring(0,10).replace("-", ".")
            binding.time.text = date
        }
    }

}