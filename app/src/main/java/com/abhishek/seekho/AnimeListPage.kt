package com.abhishek.seekho

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.seekho.databinding.FragmentAnimeListPageBinding
import com.abhishek.seekho.data.Resource
import com.abhishek.seekho.data.repository.AnimeRepository
import com.abhishek.seekho.data.di.RetrofitInstance

class AnimeListPage : Fragment() {

    private lateinit var binding: FragmentAnimeListPageBinding
    private val viewModel: AnimeViewModel by viewModels {
        AnimeViewModelFactory(AnimeRepository())
    }
    private lateinit var animeAdapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimeListPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animeAdapter = AnimeAdapter(emptyList()) { malId ->
            val bundle = Bundle()
            bundle.putInt("animeId", malId)

            val navController = findNavController()
            navController.navigate(R.id.action_animeListPage_to_animeDetailsPage, bundle)
        }
        binding.recyclerViewAnime.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAnime.adapter = animeAdapter

        viewModel.topAnime.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val animeList = resource.data.data
                    animeAdapter.setData(animeList)

                }
                is Resource.Error -> {
                    Log.e("Sikho", "Error: ${resource.message}")
                }
            }
        })

        viewModel.loadTopAnime()
    }
}
