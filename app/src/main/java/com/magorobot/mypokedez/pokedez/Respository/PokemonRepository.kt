package com.magorobot.mypokedez.pokedez.Respository

import android.util.Log
import com.magorobot.mypokedez.pokedez.ApiService
import com.magorobot.mypokedez.pokedez.BDD.PokemonDao
import com.magorobot.mypokedez.pokedez.BDD.PokemonEntity
import com.magorobot.mypokedez.pokedez.PokedexDatailDataResponse
import com.magorobot.mypokedez.pokedez.PokedexItemResponse
import com.magorobot.mypokedez.pokedez.PokedexStatistics
import com.magorobot.mypokedez.pokedez.StatNamePOder
import com.magorobot.mypokedez.pokedez.sprites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val apiService: ApiService,
    private val pokemonDao: PokemonDao
) {
    suspend fun getPokemonDetail(id: String): PokedexDatailDataResponse? {
        // Primero, intenta obtener los detalles de la base de datos local
        val localPokemon = pokemonDao.getPokemonByName(id)
        if (localPokemon != null) {
            // Convierte la entidad local a PokedexDatailDataResponse
            return PokedexDatailDataResponse(
                diname = localPokemon.name,
                sprites = sprites(localPokemon.imageUrl),
                pokedez = listOf(
                    PokedexStatistics(localPokemon.hp.toString(), StatNamePOder("hp")),
                    PokedexStatistics(localPokemon.attack.toString(), StatNamePOder("attack")),
                    PokedexStatistics(localPokemon.defense.toString(), StatNamePOder("defense")),
                    PokedexStatistics(localPokemon.speed.toString(), StatNamePOder("speed"))
                )
            )
        }

        // Si no está en la base de datos local, obtén los detalles de la API
        val response = apiService.getPokedez(id)
        if (response.isSuccessful && response.body() != null) {
            val pokemonDetail = response.body()!!
            // Guarda los detalles en la base de datos local para futuras consultas
            savePokemonToDatabase(pokemonDetail)
            return pokemonDetail
        }

        return null
    }

    private fun savePokemonToDatabase(pokemonDetail: PokedexDatailDataResponse) {
        val pokemonEntity = PokemonEntity(
            id = pokemonDetail.diname.hashCode(), // o alguna otra forma de generar un ID único
            name = pokemonDetail.diname,
            url = "", // Necesitarías obtener esta información de alguna manera
            imageUrl = pokemonDetail.sprites.url,
            hp = pokemonDetail.pokedez.find { it.stat.name == "hp" }?.baseStats?.toIntOrNull() ?: 0,
            attack = pokemonDetail.pokedez.find { it.stat.name == "attack" }?.baseStats?.toIntOrNull()
                ?: 0,
            defense = pokemonDetail.pokedez.find { it.stat.name == "defense" }?.baseStats?.toIntOrNull()
                ?: 0,
            speed = pokemonDetail.pokedez.find { it.stat.name == "speed" }?.baseStats?.toIntOrNull()
                ?: 0
        )
        pokemonDao.insertPokemon(pokemonEntity)
    }

    suspend fun getPokemonList(): List<PokedexItemResponse> {
         Log.d("Magorobot", "Obtener la lista de Pokémon")
        val localPokemon = pokemonDao.getAllPokemon()
        // Si hay datos en la base de datos local, los devuelve
        if (localPokemon.isNotEmpty()) {
            return localPokemon.map { PokedexItemResponse(it.name, it.url) }
        }
        Log.d("Magorobot", "Obtener Pokémon de API")
        val response = apiService.getPokedex("?limit=32")
        if (response.isSuccessful && response.body() != null) {
            val pokedexList = response.body()!!.pokedex
            pokedexList.forEach { pokemon ->
                val detailResponse = apiService.getPokedez(pokemon.namepokemon)
                if (detailResponse.isSuccessful && detailResponse.body() != null) {
                    val detail = detailResponse.body()!!
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
                    pokemonDao.insertPokemon(pokemonEntity)
                }
            }
            return pokedexList
        }
        Log.d("Magorobot", "No se ha encontrado ningún Pokémon")
        return emptyList()
    }


    }





