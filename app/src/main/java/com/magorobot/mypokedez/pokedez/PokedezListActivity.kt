package com.magorobot.mypokedez.pokedez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.magorobot.mypokedez.databinding.ActivityPokedezListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedezListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokedezListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: PokedexAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokedezListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        initUI()
    }

    private fun initUI() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Llama a la función para buscar superhéroes por nombre cuando se envía una consulta
                SearchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false

        })
        adapter = PokedexAdapter{}
        binding.rvPokedex.setHasFixedSize(false)
        binding.rvPokedex.layoutManager = LinearLayoutManager(this)
        binding.rvPokedex.adapter = adapter

    }

    private fun SearchByName(query: String) {
        binding.Progressbar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            var myResponse: Response<PokedexDataResponse> =
                retrofit.create(ApiService::class.java).getPokedex(query)

            if (myResponse.isSuccessful) {
                Log.i("Magorobot", "funciona Pokemon :)")
                val response: PokedexDataResponse? = myResponse.body()
                if (response != null) {
                    Log.i("Magorobot", response.toString())
                    runOnUiThread {
                       adapter.updaterList(response.pokedex)

                        binding.Progressbar.isVisible = false
                    }
                }
            } else


                Log.i("Magorobot", "No funciona :(")
            }
        }



    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API
            //.baseUrl("https://superheroapi.com/") // Base URL para la API
            .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
            .build()
    }

}
