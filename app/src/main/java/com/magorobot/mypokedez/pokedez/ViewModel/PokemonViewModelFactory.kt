package com.magorobot.mypokedez.pokedez.ViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.magorobot.mypokedez.pokedez.Respository.PokemonRepository


class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PokemonListViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                PokemonListViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailPokemonViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                DetailPokemonViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}