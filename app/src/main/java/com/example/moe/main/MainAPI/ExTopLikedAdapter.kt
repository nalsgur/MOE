package com.example.moe.MainAPI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ItemShowBinding
import com.example.moe.detail.search.entities.Search

class ExTopLikedAdapter(
    private var exhibitions: List<ExhibitionTopLiked> = emptyList(),
    private var filterExhibitions: List<ExFilterTopLike> = emptyList(),
    private val isTopLiked: Boolean
) : RecyclerView.Adapter<ExTopLikedAdapter.ViewHolder>() {

    var onItemClickListener: ((Search) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) {
            when (item) {
                is ExhibitionTopLiked -> {
                    binding.itemTitleTx.text = if (item.name.length > 8) {
                        "${item.name.substring(0, 9)} ..."
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
                is ExFilterTopLike -> {
                    binding.itemTitleTx.text = if (item.name.length > 8) {
                        "${item.name.substring(0, 9)} ..."
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
        val exhibition = if (isTopLiked) {
            exhibitions[position]
        } else {
            filterExhibitions[position]
        }
        holder.bind(exhibition)
    }

    override fun getItemCount(): Int {
        return if (isTopLiked) exhibitions.size else filterExhibitions.size
    }

    fun updateDefaultData(newData: List<ExhibitionTopLiked>) {
        this.exhibitions = newData
        notifyDataSetChanged()
    }

    fun updateFilterData(newData: List<ExFilterTopLike>) {
        this.filterExhibitions = newData
        notifyDataSetChanged()
    }
}

private fun ExhibitionTopLiked.toSearch(): Search {
    return Search(
        id = this.id,
        title = this.name,
        photo = this.photoUrl ?: "",
        startDate = this.startDate,
        endDate = this.endDate,
        follow = this.heart,
        popupStore = false
    )
}

private fun ExFilterTopLike.toSearch(): Search {
    return Search(
        id = this.id,
        title = this.name,
        photo = this.photoUrl ?: "",
        startDate = this.startDate,
        endDate = this.endDate,
        follow = this.heart,
        popupStore = false
    )
}