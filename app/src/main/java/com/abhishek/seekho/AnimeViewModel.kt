package com.abhishek.seekho

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.seekho.data.Resource
import com.abhishek.seekho.data.model.AnimeDetailsResponse
import com.abhishek.seekho.data.model.AnimeResponse
import com.abhishek.seekho.data.repository.AnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _topAnime = MutableLiveData<Resource<AnimeResponse>>()
    val topAnime: LiveData<Resource<AnimeResponse>> get() = _topAnime

    private val _animeDetails = MutableLiveData<Resource<AnimeDetailsResponse>>()
    val animeDetails: LiveData<Resource<AnimeDetailsResponse>> get() = _animeDetails

    fun loadTopAnime() {
        _topAnime.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repository.fetchTopAnime()
                    withContext(Dispatchers.Main) {
                        _topAnime.value = response
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _topAnime.value = Resource.Error("Error: ${e.message}")
                    }
                }
            }
        }
    }

    fun loadAnimeDetails(animeId: Int) {
        _animeDetails.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = repository.fetchAnimeDetails(animeId)
                    withContext(Dispatchers.Main) {
                        _animeDetails.value = response
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _animeDetails.value = Resource.Error("Error: ${e.message}")
                    }
                }
            }
        }
    }
}
