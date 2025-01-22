package com.abhishek.seekho

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abhishek.seekho.data.Resource
import com.abhishek.seekho.data.model.AnimeData
import com.abhishek.seekho.data.repository.AnimeRepository
import com.abhishek.seekho.databinding.FragmentAnimeDetailsPageBinding
import com.bumptech.glide.Glide

class AnimeDetailsPage : Fragment() {

    private lateinit var binding: FragmentAnimeDetailsPageBinding
    private val viewModel: AnimeViewModel by viewModels {
        AnimeViewModelFactory(AnimeRepository())
    }
    private var malId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            malId = it.getInt("animeId", -1)
        }
        binding = FragmentAnimeDetailsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Observe anime details
        viewModel.animeDetails.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Optionally show loading state
                }
                is Resource.Success -> {
                    val animeDetails = resource.data.data
                    updateUI(animeDetails)
                    Log.w("AnimeDetailsPage", "Anime details loaded: $animeDetails")
                }
                is Resource.Error -> {
                    Log.e("AnimeDetailsPage", "Error: ${resource.message}")
                }
            }
        })

        viewModel.loadAnimeDetails(malId)
    }

    private fun updateUI(animeDetails: AnimeData) {

        binding.tvTitle.text = animeDetails.title
        binding.tvPlot.text = animeDetails.synopsis
        binding.tvGenres.text = animeDetails.genres.joinToString(", ")
        binding.tvCast.text = "N/A"
        binding.tvEpisodes.text = "${animeDetails.episodes} Episodes"
        binding.tvRating.text = "Rating: ${animeDetails.rating}"


        if (animeDetails.trailer?.url != null) {
            val videoId = extractYouTubeVideoId(animeDetails.trailer.url)
            binding.playerView.visibility = View.VISIBLE
            binding.ivPoster.visibility = View.GONE
            val videoUrl = "https://www.youtube.com/embed/$videoId?autoplay=1&modestbranding=1"
            binding.playerView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
            }
            binding.playerView.webChromeClient = WebChromeClient()
            binding.playerView.loadUrl(videoUrl)
        } else {
            showPoster(animeDetails)
        }
    }

    private fun extractYouTubeVideoId(url: String): String? {
        val regex = "(?:youtube\\.com\\/.*(?:\\?|&)v=|youtu\\.be\\/)([a-zA-Z0-9_-]{11})".toRegex()
        val match = regex.find(url)
        return match?.groups?.get(1)?.value
    }

    private fun showPoster(animeDetails: AnimeData) {
        binding.playerView.visibility = View.GONE
        binding.ivPoster.visibility = View.VISIBLE

        Glide.with(requireContext())
            .load(animeDetails.images.jpg.large_image_url)
            .error(animeDetails.images.jpg.large_image_url)
            .into(binding.ivPoster)
    }
}
