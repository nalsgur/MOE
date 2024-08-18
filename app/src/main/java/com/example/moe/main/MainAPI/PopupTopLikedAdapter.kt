package com.example.moe.MainAPI

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ItemShowBinding
import com.example.moe.detail.search.entities.Search

class PopupTopLikedAdapter(
    private var popupStores: List<PopupStoresTopLiked> = emptyList(),
    private var filterPopupStores: List<PopupFilterTopLike> = emptyList(),
    private val isTopLiked: Boolean
) : RecyclerView.Adapter<PopupTopLikedAdapter.ViewHolder>() {

    var onItemClickListener: ((Search) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) {
            when(item) {
                is PopupStoresTopLiked -> {
                    binding.itemTitleTx.text = if (item.name.length > 11) {
                        "${item.name.substring(0, 11)} ..."
                    } else {
                        item.name
                    }

                    Glide.with(binding.itemImg.context)
                        .load(item.photoUrl)
                        .into(binding.itemImg)

                    binding.itemDateTx.text = "${item.startDate} ~ ${item.endDate}"

                    binding.itemHeart.setImageResource(
                        if (item.heart) R.drawable.main_full_heart2 else R.drawable.main_heart2
                    )

                    binding.itemHeart.setOnClickListener {
                        item.heart = !item.heart
                        notifyItemChanged(adapterPosition)
                    }

                    binding.root.setOnClickListener {
                        onItemClickListener?.invoke(item.toSearch())
                    }
                }
                is PopupFilterTopLike -> {
                    binding.itemTitleTx.text = if (item.name.length > 8) {
                        "${item.name.substring(0, 11)} ..."
                    } else {
                        item.name
                    }

                    Glide.with(binding.itemImg.context)
                        .load(item.photoUrl)
                        .into(binding.itemImg)

                    binding.itemDateTx.text = "${item.startDate} ~ ${item.endDate}"

                    binding.itemHeart.setImageResource(
                        if (item.heart) R.drawable.main_full_heart2 else R.drawable.main_heart2
                    )

                    binding.itemHeart.setOnClickListener {
                        item.heart = !item.heart
                        notifyItemChanged(adapterPosition)
                    }

                    binding.root.setOnClickListener {
                        onItemClickListener?.invoke(item.toSearch())
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val popupStore = if (isTopLiked) {
            popupStores[position]
        } else {
            filterPopupStores[position]
        }
        holder.bind(popupStore)
    }

    override fun getItemCount(): Int = popupStores.size

    fun updateDefaultData(newData: List<PopupStoresTopLiked>) {
        this.popupStores = newData
        notifyDataSetChanged()
    }

    fun updateFilterData(newData: List<PopupFilterTopLike>) {
        this.filterPopupStores = newData
        notifyDataSetChanged()
    }
}

private fun PopupStoresTopLiked.toSearch(): Search {
    return Search(
        id = this.id,
        title = this.name,
        photo = this.photoUrl ?: "",
        startDate = this.startDate,
        endDate = this.endDate,
        follow = this.heart,
        popupStore = true
    )
}

private fun PopupFilterTopLike.toSearch(): Search {
    return Search(
        id = this.id,
        title = this.name,
        photo = this.photoUrl ?: "",
        startDate = this.startDate,
        endDate = this.endDate,
        follow = this.heart,
        popupStore = true
    )
}