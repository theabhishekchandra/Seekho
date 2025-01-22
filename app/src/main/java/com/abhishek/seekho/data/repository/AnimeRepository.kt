package com.abhishek.seekho.data.repository

import com.abhishek.seekho.data.Resource
import com.abhishek.seekho.data.api.JikanApi
import com.abhishek.seekho.data.di.RetrofitInstance
import com.abhishek.seekho.data.model.AnimeDetailsResponse
import com.abhishek.seekho.data.model.AnimeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchTopAnime(): Resource<AnimeResponse> {
        return try {
            val response = api.getTopAnime()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch data: ${e.message}")
        }
    }

    suspend fun fetchAnimeDetails(animeId: Int): Resource<AnimeDetailsResponse> {
        return try {
            val response = api.getAnimeDetails(animeId)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch details: ${e.message}")
        }
    }
}
