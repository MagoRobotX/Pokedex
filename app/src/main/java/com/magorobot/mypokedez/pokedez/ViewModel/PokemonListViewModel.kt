package com.magorobot.mypokedez.pokedez.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magorobot.mypokedez.pokedez.PokedexItemResponse
import com.magorobot.mypokedez.pokedez.Respository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<List<PokedexItemResponse>>(emptyList())
    val pokemonList: StateFlow<List<PokedexItemResponse>> = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                Log.d("Magorobot", "Empezando a cargar Pokemon")
                val pokemonList = repository.getPokemonList()
                Log.d("Magorobot", "Loaded ${pokemonList.size} Pokemon")
                _pokemonList.value = pokemonList
            } catch (e: Exception) {
                 Log.e("Magorobot", "Error al cargar Pokémon", e)
                // Maneja el error aquí
            } finally {
                _isLoading.value = false
            }
        }
    }
}