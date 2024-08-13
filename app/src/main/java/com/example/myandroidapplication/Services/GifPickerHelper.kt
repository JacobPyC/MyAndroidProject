// GifPickerHelper.kt
package com.example.myandroidapplication.Helpers

import GifAdapter
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myandroidapplication.R

class GifPickerHelper(private val context: Context) {

    fun fetchGifs(onGifsFetched: (List<String>) -> Unit) {
        val apiKey = "Qw3bZ9z69uDtyS7ZxP05lMlfHBc7UK7b" // Replace with your Giphy API key
        val limit = 10
        val url = "https://api.giphy.com/v1/gifs/trending?api_key=$apiKey&limit=$limit"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val dataArray = response.getJSONArray("data")
                val gifUrls = mutableListOf<String>()

                for (i in 0 until dataArray.length()) {
                    val gifObject = dataArray.getJSONObject(i)
                    val imagesObject = gifObject.getJSONObject("images")
                    val originalObject = imagesObject.getJSONObject("original")
                    val gifUrl = originalObject.getString("url")
                    gifUrls.add(gifUrl)
                }

                onGifsFetched(gifUrls)
            },
            { error ->
                // Handle error
            }
        )

        Volley.newRequestQueue(context).add(request)
    }

    fun showGifSelectionDialog(gifUrls: List<String>, onGifSelected: (String) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_gif_selection, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewGifs)
        val searchView = dialogView.findViewById<SearchView>(R.id.searchViewGifs)

        val gifAdapter = GifAdapter(gifUrls) { selectedGifUrl ->
            onGifSelected(selectedGifUrl)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gifAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = gifUrls.filter { it.contains(newText ?: "", ignoreCase = true) }
                gifAdapter.updateGifList(filteredList)
                return true
            }
        })

        AlertDialog.Builder(context)
            .setTitle("Select a GIF")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .show()
    }
}
