package com.example.moe.main.MainAPI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.moe.databinding.ItemFollowCardModeBinding

class FollowCardAdapter(
    private val items: List<FollowCardItem>,
    private val viewPager2: ViewPager2,
    private val onItemClick: (FollowCardItem) -> Unit
) : RecyclerView.Adapter<FollowCardAdapter.cardViewHolder>() {

    class cardViewHolder(val binding: ItemFollowCardModeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cardViewHolder {
        val binding = ItemFollowCardModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: cardViewHolder, position: Int) {
        val item = items[position % items.size]

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.binding.cardView)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun updateItems(newItems: List<FollowCardItem>) {
        (items as MutableList).clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class FollowCardItem (val imageUrl : String, val title : String, val date : String)
