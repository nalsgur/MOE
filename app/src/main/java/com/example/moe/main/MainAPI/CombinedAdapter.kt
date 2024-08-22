package com.example.moe.MainAPI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moe.R
import com.example.moe.databinding.ItemShowBinding
import com.example.moe.main.MainAPI.CombinedResponse

class CombinedAdapter(
    private val sharedViewModel: SharedViewModel,
    private val items: MutableList<CombinedResponse> = mutableListOf()
): RecyclerView.Adapter<CombinedAdapter.CombinedViewHolder>() {

    private val filteredItems: MutableList<CombinedResponse> = mutableListOf()

    inner class CombinedViewHolder(private val binding: ItemShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(view: CombinedResponse) {

            binding.itemTitleTx.text = if (view.name.length > 11) {
                "${view.name.substring(0, 11)} ..."
            } else {
                view.name
            }

            Glide.with(binding.itemImg.context)
                .load(view.photoUrl)
                .into(binding.itemImg)

            binding.itemDateTx.text = "${view.startDate} ~ ${view.endDate}"

            val isLiked = sharedViewModel.followGetHeartStatus("topLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("latest", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupTopLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupLatest", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("topLikedFilter", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("latestFilter", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupFilterTopLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupFilterLatest", view.id.toString(), view.name)

            binding.itemHeart.setImageResource(
                if (isLiked) R.drawable.main_full_heart2 else R.drawable.main_heart2
            )

            binding.itemHeart.setOnClickListener {
                val newStatus = !isLiked

                sharedViewModel.followUpdateHeartStatus("topLiked", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("latest", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("popupTopLiked", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("popupLatest", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("topLikedFilter", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("latestFilter", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("popupFilterTopLiked", view.id.toString(), view.name, newStatus)
                sharedViewModel.followUpdateHeartStatus("popupFilterLatest", view.id.toString(), view.name, newStatus)
                updateFilteredItems()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombinedViewHolder {
        val view = ItemShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CombinedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CombinedViewHolder, position: Int) {
        val view = filteredItems[position]
        holder.bind(view)

    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    fun updateData(newItems: List<CombinedResponse>) {
        items.clear()
        items.addAll(newItems)
        updateFilteredItems()
    }

    fun updateFilteredItems() {
        filteredItems.clear()

        val seenItems = mutableSetOf<String>()

        items.forEach { view ->
            val uniqueKey = "${view.id}-${view.name}"
            val isLiked = sharedViewModel.followGetHeartStatus("topLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("latest", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupTopLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupLatest", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("topLikedFilter", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("latestFilter", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupFilterTopLiked", view.id.toString(), view.name)
                    || sharedViewModel.followGetHeartStatus("popupFilterLatest", view.id.toString(), view.name)

            if (isLiked) {
                if (!seenItems.contains(uniqueKey)) {
                    seenItems.add(uniqueKey)
                    filteredItems.add(view)
                }
            }
        }
        notifyDataSetChanged()
    }
}
