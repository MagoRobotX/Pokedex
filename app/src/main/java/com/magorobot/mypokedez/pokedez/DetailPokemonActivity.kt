package com.magorobot.mypokedez.pokedez

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.magorobot.mypokedez.R
import com.magorobot.mypokedez.databinding.ActivityDetailPokemonBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailPokemonActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityDetailPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getPokedexInformation(id)
    }


    private fun getPokedexInformation(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val detail = getRetrofit().create(ApiService::class.java).getPokedez(id)
            if (detail.body() != null) {
                runOnUiThread { createUI(detail.body()!!) }
             //   runOnUiThread { createUI(pokeDetail.body()!!) }

            }
        }

    }
     private fun createUI(poke: PokedexDatailDataResponse) {
    // Cargar la imagen del Pokémon con Picasso
        Picasso.get()
            .load(poke.sprites.url)
            .into(binding.ivPokeImagen)
    }

}


private fun getRetrofit(): Retrofit {
    return Retrofit
        .Builder()
        .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API
        .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
        .build()
}
