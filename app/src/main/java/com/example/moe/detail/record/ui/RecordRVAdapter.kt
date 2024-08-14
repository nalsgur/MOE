package com.example.moe.detail.record.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.SearchedItemBinding
import com.example.moe.detail.PageViewModel
import com.example.moe.detail.record.entities.Record


class RecordRVAdapter (record: List<Record>, private val viewModel: PageViewModel): RecyclerView.Adapter<RecordRVAdapter.ViewHolder>() {

    private val recordList = record.chunked(4)

    interface MyItemClickListener{
        fun onClickItem(record: Record)
        fun onClickHeart(record: Record)
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
        holder.bind(recordList[viewModel.pageIndex.value!!-1][position])
        holder.binding.favoriteIv.setOnClickListener {
            mItemClickListener.onClickHeart(recordList[viewModel.pageIndex.value!!-1][position])
        }
        holder.binding.searchItemCl.setOnClickListener {
            mItemClickListener.onClickItem(recordList[viewModel.pageIndex.value!!-1][position])
        }
    }


    override fun getItemCount(): Int = recordList[viewModel.pageIndex.value!!-1].size

    inner class ViewHolder(val binding: SearchedItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(record: Record){
            Glide.with(binding.image.context)
                .load(record.photo)
                .into(binding.image)
            binding.itemTitle.text = record.name
            val date = record.startDate.substring(0,10).replace("-", ".") + " ~ " + record.endDate.substring(0,10).replace("-", ".")
            binding.time.text = date
            updateLikeIcon(record.heart)

            binding.favoriteIv.setOnClickListener {
                record.heart = !record.heart
                updateLikeIcon(record.heart)
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