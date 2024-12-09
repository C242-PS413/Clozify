package com.c242ps413.clozify.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val listener: OnItemClickListener, private val onFavoriteClick: (Favorite) -> Unit) :
    ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(favorite: Favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.bind(favorite)

        holder.binding.ivFavorite.setOnClickListener {
            onFavoriteClick(favorite)
        }
    }

    inner class FavoriteViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            binding.tvItemDescription.text = favorite.name
            Glide.with(binding.root.context)
                .load(favorite.image)
                .into(binding.image)

            binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
            binding.ivFavorite.tag = "Saved"

            binding.root.setOnClickListener {
                listener.onItemClick(favorite)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}
