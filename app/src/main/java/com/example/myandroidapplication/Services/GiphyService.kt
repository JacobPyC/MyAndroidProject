package com.example.myandroidapplication.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {
    @GET("v1/gifs/trending")
    fun getTrendingGifs(@Query("api_key") apiKey: String): Call<GiphyResponse>

    @GET("v1/gifs/search")
    fun searchGifs(@Query("api_key") apiKey: String, @Query("q") query: String): Call<GiphyResponse>
}
