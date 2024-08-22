package com.example.moe.main.dummy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ItemShowBinding

class ItemAdapter(
    private val items: List<ItemData>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemData) {
            Glide.with(binding.itemImg.context)
                .load(item.imageUrl)
                .into(binding.itemImg)

            binding.itemTitleTx.text = if(item.title.length > 11) {
                "${item.title.substring(0, 11)} ..."
            } else { item.title }

            binding.itemDateTx.text = item.date

            binding.itemHeart.setOnClickListener {
                item.isHeartSelected = !item.isHeartSelected

                if(item.isHeartSelected) {
                    binding.itemHeart.setImageResource(R.drawable.main_full_heart2)
                } else {
                    binding.itemHeart.setImageResource(R.drawable.main_heart2)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}