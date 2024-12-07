package com.c242ps413.clozify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c242ps413.clozify.data.api.response.RecommendationItem
import com.c242ps413.clozify.databinding.ItemRecommendationBinding

class HomeAdapter(private val listener: OnItemClickListener) : ListAdapter<RecommendationItem, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(event: RecommendationItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, listener)
    }

    class MyViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: RecommendationItem, listener: OnItemClickListener) {
            binding.tvItemDescription.text = event.name
            Glide.with(binding.root.context)
                .load(event.image)
                .into(binding.image)

            // Set click listener
            binding.root.setOnClickListener {
                listener.onItemClick(event) // Panggil listener saat item diklik
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationItem>() {
            override fun areItemsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
