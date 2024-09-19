package com.magorobot.mypokedez.pokedez.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.magorobot.mypokedez.pokedez.PokedexDatailDataResponse
import com.magorobot.mypokedez.pokedez.Respository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

     private val _pokemonDetail = MutableStateFlow<PokedexDatailDataResponse?>(null)
     val pokemonDetail: StateFlow<PokedexDatailDataResponse?> = _pokemonDetail


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadPokemonDetail(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val detail = repository.getPokemonDetail(id)
                if (detail != null) {
                    _pokemonDetail.value = detail

                } else {
                    _error.value = "No se pudo cargar los detalles del Pokémon"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}