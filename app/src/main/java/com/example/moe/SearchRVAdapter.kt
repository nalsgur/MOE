package com.example.moe


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.databinding.SearchedItemBinding


class SearchRVAdapter (search: List<Search>, private val viewModel: SearchViewModel): RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    private val searchList = search.chunked(6)

    interface MyItemClickListener{
        fun onClickItem(search: Search)
        //fun onClickHeart(search: Search)
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
        holder.bind(searchList[viewModel.pageIndex.value!!-1][position])
//        holder.binding.favoriteIv.setOnClickListener {
//            mItemClickListener.onClickHeart(searchList[viewModel.pageIndex.value!!-1][position])
//        }
        holder.binding.searchItemCl.setOnClickListener {
            mItemClickListener.onClickItem(searchList[viewModel.pageIndex.value!!-1][position])
        }
    }


    override fun getItemCount(): Int = searchList[viewModel.pageIndex.value!!-1].size

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