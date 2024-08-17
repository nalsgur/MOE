package com.example.moe.detail.record.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.databinding.ItemRecordPhotoBinding
import com.example.moe.detail.record.entities.Record


class PhotoRVAdapter(private val photos: List<String>): RecyclerView.Adapter<PhotoRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecordPhotoBinding = ItemRecordPhotoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    interface MyItemClickListener{
        fun onClickItem(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recordPhoto.setOnClickListener {
            mItemClickListener.onClickItem(position)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int = photos.size


    inner class ViewHolder(val binding: ItemRecordPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            Glide.with(binding.recordPhoto.context)
                .load(photos[position])
                .into(binding.recordPhoto)
        }
    }

}