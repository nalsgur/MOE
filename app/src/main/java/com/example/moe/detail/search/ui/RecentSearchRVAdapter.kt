package com.example.moe.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.SearchedItemBinding
import com.example.moe.detail.search.entities.Search


class RecentSearchRVAdapter (private val search: List<Search>): RecyclerView.Adapter<RecentSearchRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onClickItem(search: Search)
        fun onClickHeart(search: Search)
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
        holder.binding.favoriteIv.setOnClickListener {
            mItemClickListener.onClickHeart(search[position])
        }
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
            updateLikeIcon(search.follow)

            binding.favoriteIv.setOnClickListener {
                search.follow = !search.follow
                updateLikeIcon(search.follow)
            }
        }

        private fun updateLikeIcon(isLiked: Boolean){
            if(isLiked){
                binding.favoriteIv.setImageResource(R.drawable.search_full_heart)
            } else {
                binding.favoriteIv.setImageResource(R.drawable.search_heart)
            }
        }
    }

}