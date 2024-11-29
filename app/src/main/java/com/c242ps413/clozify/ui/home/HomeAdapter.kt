package com.c242ps413.clozify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.model.RecommendationItem
import com.c242ps413.clozify.databinding.ItemRecommendationBinding

class HomeAdapter :
    ListAdapter<RecommendationItem, HomeAdapter.HomeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class HomeViewHolder(private val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendationItem) {
            binding.tvItemDescription.text = item.description
            Glide.with(binding.root.context)
                .load(item.imgShirt) // Memuat gambar dari drawable
                .into(binding.imgShirt)
            Glide.with(binding.root.context)
                .load(item.imgPants) // Memuat gambar dari drawable
                .into(binding.imgPants)

            binding.cardViewFavorite.setOnClickListener {
                val tag = binding.ivFavorite.tag
                if (tag == "Not Saved") {
                    binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                    binding.ivFavorite.tag = "Saved"
                } else {
                    binding.ivFavorite.setImageResource(R.drawable.ic_favoriteborder)
                    binding.ivFavorite.tag = "Not Saved"
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RecommendationItem>() {
        override fun areItemsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean {
            return oldItem.description == newItem.description  // Pastikan description unik, atau tambah ID
        }

        override fun areContentsTheSame(oldItem: RecommendationItem, newItem: RecommendationItem): Boolean {
            return oldItem == newItem
        }
    }

}
