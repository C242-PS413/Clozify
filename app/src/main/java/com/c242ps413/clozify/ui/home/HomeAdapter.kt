package com.c242ps413.clozify.ui.home

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.api.response.RecommendationItem
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.repository.FavoriteRepository
import com.c242ps413.clozify.databinding.ItemRecommendationBinding

class HomeAdapter(private val listener: OnItemClickListener, private val application: Application) : ListAdapter<RecommendationItem, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

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

        val favoriteRepository = FavoriteRepository(application)
        val imageKey = event.image ?: ""

        favoriteRepository.getFavoriteByImage(imageKey).observe(holder.itemView.context as LifecycleOwner) { favorite ->
            if (favorite != null) {
                holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                holder.binding.ivFavorite.tag = "Saved"
            } else {
                holder.binding.ivFavorite.setImageResource(R.drawable.ic_favoriteborder)
                holder.binding.ivFavorite.tag = "Not Saved"
            }

            holder.binding.ivFavorite.setOnClickListener {
                if (holder.binding.ivFavorite.tag == "Saved") {
                    favoriteRepository.delete(favorite)
                    holder.binding.ivFavorite.tag = "Not Saved"
                    holder.binding.ivFavorite.setImageResource(R.drawable.ic_favoriteborder)
                } else {
                    val favoriteItem = Favorite(name = event.name, image = event.image)
                    favoriteRepository.insert(favoriteItem)
                    holder.binding.ivFavorite.tag = "Saved"
                    holder.binding.ivFavorite.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    class MyViewHolder(val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: RecommendationItem, listener: OnItemClickListener) {
            binding.tvItemDescription.text = event.name
            Glide.with(binding.root.context)
                .load(event.image)
                .into(binding.image)

            binding.root.setOnClickListener {
                listener.onItemClick(event)
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
