package com.abhishek.seekho.data.api

import com.abhishek.seekho.data.model.AnimeDetailsResponse
import com.abhishek.seekho.data.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApi {
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeResponse

    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(@Path("anime_id") animeId: Int): AnimeDetailsResponse
}
