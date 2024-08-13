package com.example.myandroidapplication

import GifAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myandroidapplication.databinding.FragmentGifListBinding
import com.example.myandroidapplication.network.GiphyResponse
import com.example.myandroidapplication.network.RetrofitClient
import androidx.fragment.app.setFragmentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.widget.SearchView

class GifListFragment : Fragment() {

    private lateinit var binding: FragmentGifListBinding
    private val apiKey = "Qw3bZ9z69uDtyS7ZxP05lMlfHBc7UK7b"
    private lateinit var adapter: GifAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGifListBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerViewGifs
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter with an empty list and set click listener to handle GIF selection
        adapter = GifAdapter(emptyList()) { gifUrl ->
            // Set the selected GIF URL as a fragment result
            setFragmentResult("gifRequestKey", Bundle().apply {
                putString("selectedGifUrl", gifUrl)
            })

            requireActivity().supportFragmentManager.popBackStack()
        }
        recyclerView.adapter = adapter

        // Set up the search bar
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchGifs(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fetchGifs(newText.orEmpty())
                return true
            }
        })

        return binding.root
    }

    private fun fetchGifs(query: String) {
        val call = if (query.isEmpty()) {
            RetrofitClient.instance.getTrendingGifs(apiKey)
        } else {
            RetrofitClient.instance.searchGifs(apiKey, query)
        }

        call.enqueue(object : Callback<GiphyResponse> {
            override fun onResponse(call: Call<GiphyResponse>, response: Response<GiphyResponse>) {
                if (response.isSuccessful) {
                    val gifs = response.body()?.data ?: emptyList()
                    if (gifs.isNotEmpty()) {
                        adapter.updateGifList(gifs.map { it.images.original.url })
                    } else {
                        // Handle empty response
                        Log.d("GifListFragment", "No GIFs found for query: $query")
                    }
                } else {
                    Log.e(
                        "GifListFragment",
                        "API Response Error: ${response.code()} - ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                Log.e("GifListFragment", "Network Failure: ${t.message}")
            }
        })
    }

}
