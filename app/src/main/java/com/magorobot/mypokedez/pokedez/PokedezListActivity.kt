package com.magorobot.mypokedez.pokedez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.magorobot.mypokedez.databinding.ActivityPokedezListBinding
import com.magorobot.mypokedez.pokedez.DetailPokemonActivity.Companion.EXTRA_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Definición de la actividad principal para mostrar la lista de Pokémon
class PokedezListActivity : AppCompatActivity() {
    // Declaración de variables para el binding, Retrofit y el adaptador
    private lateinit var binding: ActivityPokedezListBinding

    // Declara una propiedad para la instancia de Retrofit, que se inicializará más tarde
    private lateinit var retrofit: Retrofit

    // Declara una propiedad para el adaptador del RecyclerView, que se inicializará más tarde
    private lateinit var adapter: PokedexAdapter

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout y establecerlo como contenido de la actividad
        binding = ActivityPokedezListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inicializar Retrofit
        retrofit = getRetrofit()
        // Inicializar la interfaz de usuario
        initUI()
    }

    // Método para inicializar la interfaz de usuario
    private fun initUI() {
        // Configurar el listener para el SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Llama a la función para buscar superhéroes por nombre cuando se envía una consulta
                SearchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false

        })
        // Inicializa el adaptador con una función lambda para la navegación
        adapter = PokedexAdapter { navigateToDetail(it) }
        // Indica al RecyclerView que el tamaño de su layout no cambiará // Esto permite optimizaciones internas en el RecyclerView
        binding.rvPokedex.setHasFixedSize(true)
        // Establece un GridLayoutManager como el layout manager del RecyclerView // Esto organiza los elementos en una cuadrícula de 3 columnas
        binding.rvPokedex.layoutManager = GridLayoutManager(this, 3) // 2 columnas en la cuadrícula
        // Asigna el adaptador personalizado al RecyclerView // Este adaptador se encargará de crear y reciclar las vistas para cada elemento de la lista
        binding.rvPokedex.adapter = adapter

    }

    // Método para buscar Pokémon por nombre
    private fun SearchByName(query: String) {
        // Mostrar la barra de progreso
        binding.Progressbar.isVisible = true
        // Lanzar una corrutina en el hilo de IO
        CoroutineScope(Dispatchers.IO).launch {
            // Realizar la llamada a la API
            var myResponse: Response<PokedexDataResponse> =
                retrofit.create(ApiService::class.java).getPokedex(query) // Realiza una llamada a la API usando Retrofit para obtener datos de Pokémon
            // Verifica si la respuesta de la API fue exitosa
            if (myResponse.isSuccessful) {
                // Imprime un mensaje de éxito en el log
                Log.i("Magorobot", "funciona Pokemon :)")
                // Extrae el cuerpo de la respuesta
                val response: PokedexDataResponse? = myResponse.body()
                // Verifica si el cuerpo de la respuesta no es nulo
                if (response != null) {
                    // Imprime la respuesta completa en el log
                    Log.i("Magorobot", response.toString())
                     // Actualiza la UI en el hilo principal
                    runOnUiThread {
                        // Actualiza la lista en el adaptador con los nuevos datos de Pokémon
                        adapter.updaterList(response.pokedex)
                         // Oculta la barra de progreso
                        binding.Progressbar.isVisible = false
                    }
                }
            } else
                // Imprime un mensaje de error en el log si la llamada a la API falló
                Log.i("Magorobot", "No funciona :(")
        }
    }

    // Método para obtener una instancia configurada de Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API
            //.baseUrl("https://superheroapi.com/") // Base URL para la API
            .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
            .build()
    }

    private fun navigateToDetail(id: String) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }

}