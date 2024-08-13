// GiphyResponse.kt
package com.example.myandroidapplication.network

data class GiphyResponse(
    val data: List<Gif>
)

data class Gif(
    val id: String,
    val images: Images
)

data class Images(
    val original: Image
)

data class Image(
    val url: String
)
