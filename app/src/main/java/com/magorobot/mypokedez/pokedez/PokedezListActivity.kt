package com.magorobot.mypokedez.pokedez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.magorobot.mypokedez.databinding.ActivityPokedezListBinding
import com.magorobot.mypokedez.pokedez.BDD.PokemonDatabase
import com.magorobot.mypokedez.pokedez.BDD.PokemonEntity
import com.magorobot.mypokedez.pokedez.DetailPokemonActivity.Companion.EXTRA_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var database: PokemonDatabase

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout y establecerlo como contenido de la actividad
        binding = ActivityPokedezListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inicializar Retrofit
        retrofit = getRetrofit()
        database = PokemonDatabase.getInstance(this)
        // Inicializar la interfaz de usuario
        initUI()
         loadPokemon()
    }

    private fun initUI(){
         // Inicializa el adaptador con una función lambda para la navegación
        adapter = PokedexAdapter { navigateToDetail(it) }
        // Indica al RecyclerView que el tamaño de su layout no cambiará
        binding.rvPokedex.setHasFixedSize(true)
        // Establece un GridLayoutManager como el layout manager del RecyclerView
        binding.rvPokedex.layoutManager = GridLayoutManager(this, 3)
        // Asigna el adaptador personalizado al RecyclerView
        binding.rvPokedex.adapter = adapter

        // Puedes cargar todos los Pokémon aquí o manejar la carga inicial sin filtro de búsqueda
       // loadAllPokemon()
    }
    // Método para cargar todos los Pokémon
private fun loadPokemon() {
    // Lanzar una corrutina en el hilo de IO para operaciones de red y base de datos
    CoroutineScope(Dispatchers.IO).launch {
        // Mostrar la barra de progreso mientras se cargan los datos
        binding.Progressbar.isVisible = true

        // Intentar obtener Pokémon desde la base de datos local
        val localPokemon = database.pokemonDao().getAllPokemon()

        // Si hay Pokémon en la base de datos local
        if (localPokemon.isNotEmpty()) {
            // Convertir los datos de la base de datos en objetos `PokedexItemResponse`
            val pokedexList = localPokemon.map {
                PokedexItemResponse(it.name, it.url)
            }
            // Actualizar la UI con la lista de Pokémon obtenida de la base de datos
            updateUI(pokedexList)
        } else {
            try {
                // Crear una instancia del servicio de API usando Retrofit
                val apiService = retrofit.create(ApiService::class.java)

                // Realizar la llamada a la API para obtener la lista de Pokémon
                val response = apiService.getPokedex("")

                // Si la respuesta de la API es exitosa y no es nula
                if (response.isSuccessful && response.body() != null) {
                    // Obtener la lista de Pokémon desde la respuesta de la API
                    val pokedexList = response.body()!!.pokedex

                    // Guardar cada Pokémon en la base de datos local
                    pokedexList.forEach { pokemon ->
                        // Obtener los detalles de cada Pokémon desde la API
                        val detailResponse = apiService.getPokedez(pokemon.namepokemon)

                        // Si la respuesta de los detalles es exitosa y no es nula
                        if (detailResponse.isSuccessful && detailResponse.body() != null) {
                            // Obtener los detalles del Pokémon
                            val detail = detailResponse.body()!!

                            // Crear una entidad de Pokémon para la base de datos
                            val pokemonEntity = PokemonEntity(
                                id = pokemon.id,
                                name = pokemon.namepokemon,
                                url = pokemon.url,
                                imageUrl = detail.sprites.url,
                                hp = detail.pokedez.find { it.stat.name == "hp" }?.baseStats?.toIntOrNull() ?: 0,
                                attack = detail.pokedez.find { it.stat.name == "attack" }?.baseStats?.toIntOrNull() ?: 0,
                                defense = detail.pokedez.find { it.stat.name == "defense" }?.baseStats?.toIntOrNull() ?: 0,
                                speed = detail.pokedez.find { it.stat.name == "speed" }?.baseStats?.toIntOrNull() ?: 0
                            )
                            // Insertar la entidad en la base de datos
                            database.pokemonDao().insertPokemon(pokemonEntity)
                        }
                    }

                    // Actualizar la UI con la lista de Pokémon obtenida de la API
                    updateUI(pokedexList)
                } else {
                    // Si la respuesta de la API no es exitosa, registrar el error y actualizar la UI con una lista vacía
                    Log.e("PokedezListActivity", "Error: ${response.errorBody()?.string()}")
                    updateUI(emptyList())
                }
            } catch (e: Exception) {
                // Si ocurre una excepción, registrar el error y actualizar la UI con una lista vacía
                Log.e("PokedezListActivity", "Exception: ${e.message}")
                updateUI(emptyList())
            }
        }
    }
}

    private suspend fun updateUI(pokedexList: List<PokedexItemResponse>) {
    // Cambiar al hilo principal para actualizar la UI, ya que las operaciones de UI deben ejecutarse en el hilo principal
    withContext(Dispatchers.Main) {
        // Actualizar la lista en el adaptador con los nuevos datos de Pokémon
        adapter.updaterList(pokedexList)

        // Ocultar la barra de progreso después de que se ha actualizado la lista
        binding.Progressbar.isVisible = false
    }
}


    // Método para obtener una instancia configurada de Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API

            .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
            .build()
    }

    private fun navigateToDetail(id: String) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }

}