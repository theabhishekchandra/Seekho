package com.abhishek.seekho

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.seekho.data.model.Anime
import com.abhishek.seekho.databinding.ItemAnimeBinding
import com.bumptech.glide.Glide

class AnimeAdapter(private var animeList: List<Anime>,
                   private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    fun setData(newData: List<Anime>) {
        animeList = newData
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
    }

    override fun getItemCount(): Int = animeList.size

    inner class AnimeViewHolder(private val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            val id = anime.mal_id
            binding.tvTitle.text = anime.title
            binding.tvEpisodes.text = anime.episodes?.toString() ?: "N/A"
            binding.tvRating.text = anime.rating ?: "N/A"
            Glide.with(binding.ivPoster.context)
                .load(anime.images.jpg.large_image_url)
                .into(binding.ivPoster)

            itemView.setOnClickListener {
                onItemClick(anime.mal_id)
            }
        }
    }
}