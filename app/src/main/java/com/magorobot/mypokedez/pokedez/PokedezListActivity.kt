package com.magorobot.mypokedez.pokedez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.magorobot.mypokedez.databinding.ActivityPokedezListBinding
import com.magorobot.mypokedez.pokedez.Activity.DetailPokemonActivity
import com.magorobot.mypokedez.pokedez.BDD.PokemonDatabase
import com.magorobot.mypokedez.pokedez.BDD.PokemonEntity
import com.magorobot.mypokedez.pokedez.Respository.PokemonRepository
import com.magorobot.mypokedez.pokedez.ViewModel.PokemonListViewModel
import com.magorobot.mypokedez.pokedez.ViewModel.PokemonViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    //private lateinit var database: PokemonDatabase
    private lateinit var viewModel: PokemonListViewModel

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout y establecerlo como contenido de la actividad
        binding = ActivityPokedezListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inicializar Retrofit
        retrofit = getRetrofit()
        val database = PokemonDatabase.getInstance(this)
        val repository =
            PokemonRepository(retrofit.create(ApiService::class.java), database.pokemonDao())
        viewModel = ViewModelProvider(this, PokemonViewModelFactory(repository))
            .get(PokemonListViewModel::class.java)
        // Inicializar la interfaz de usuario
        initUI()
        observeViewModel()
        viewModel.loadPokemon()

        // loadPokemon()
    }

    private fun initUI() {
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

    // Método para observar cambios en el ViewModel
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pokemonList.collect { pokemonList ->
                adapter.updaterList(pokemonList)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.Progressbar.isVisible = isLoading
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
    private fun navigateToDetail(id: String) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra(DetailPokemonActivity.EXTRA_ID, id)
        startActivity(intent)
    }
      // Método para obtener una instancia configurada de Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API
            .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
            .build()
    }

}