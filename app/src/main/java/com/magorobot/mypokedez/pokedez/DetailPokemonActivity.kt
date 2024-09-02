package com.magorobot.mypokedez.pokedez

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.magorobot.mypokedez.R
import com.magorobot.mypokedez.databinding.ActivityDetailPokemonBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailPokemonActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }
private lateinit var binding: ActivityDetailPokemonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_pokemon)

    }
}
