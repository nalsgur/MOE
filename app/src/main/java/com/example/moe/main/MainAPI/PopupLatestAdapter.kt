package com.example.moe.MainAPI

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ItemShowBinding
import com.example.moe.detail.search.entities.Search

class PopupLatestAdapter(
    private var popupStores: List<PopupStoresLatest> = emptyList(),
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<PopupLatestAdapter.ViewHolder>() {

    var onItemClickListener: ((Search) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PopupStoresLatest) {

            binding.itemTitleTx.text = if (item.name.length > 11) {
                "${item.name.substring(0, 11)} ..."
            } else {
                item.name
            }

            Glide.with(binding.itemImg.context)
                .load(item.photoUrl)
                .into(binding.itemImg)

            binding.itemDateTx.text = "${item.startDate} ~ ${item.endDate}"

            val isLiked = sharedViewModel.getHeartStatus("popupLatest", item.id.toString())
            binding.itemHeart.setImageResource(
                if (isLiked) R.drawable.main_full_heart2 else R.drawable.main_heart2
            )

            binding.itemHeart.setOnClickListener {
                val newStatus = !isLiked
                sharedViewModel.UpdateHeartStatus("popupLatest", item.id.toString(), newStatus)
                sharedViewModel.followUpdateHeartStatus("popupLatest", item.id.toString(), item.name, newStatus)
                binding.itemHeart.setImageResource(
                    if (newStatus) R.drawable.main_full_heart2 else R.drawable.main_heart2
                )
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item.toSearch())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popupStores[position])
    }

    override fun getItemCount(): Int = popupStores.size

    fun updateDefaultData(newData: List<PopupStoresLatest>) {
        this.popupStores = newData
        notifyDataSetChanged()
    }


}

private fun PopupStoresLatest.toSearch(): Search {
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

