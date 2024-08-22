package com.example.moe.main.MainAPI

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.moe.MainAPI.ExhibitionTopLiked
import com.example.moe.MainAPI.SharedViewModel
import com.example.moe.databinding.ItemFollowCardModeBinding
import com.example.moe.databinding.ItemShowBinding
import com.example.moe.detail.follow.ui.FollowItemActivity

class FollowCardAdapter(
    private val context: Context,
    private val items:  List<CardItem>
) : RecyclerView.Adapter<FollowCardAdapter.FollowCardViewHolder>() {

    class FollowCardViewHolder(val binding: ItemFollowCardModeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowCardViewHolder {
        val binding = ItemFollowCardModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowCardViewHolder, position: Int) {
        val item = items[position % items.size]

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.binding.cardView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FollowItemActivity::class.java).apply {
                putExtra("IMAGE_URL", item.imageUrl)
                putExtra("TITLE", item.name)
                putExtra("DATE", item.date)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun updateData(newItems: List<CardItem>) {
        (items as MutableList).clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
