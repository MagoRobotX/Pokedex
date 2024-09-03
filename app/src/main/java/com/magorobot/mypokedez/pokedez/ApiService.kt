package com.magorobot.mypokedez.pokedez

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Obtiene la lista de Pokémones según un nombre
    @GET("/api/v2/{pokemon}")
    suspend fun getPokedex(@Path("pokemon") pokedexName: String): Response<PokedexDataResponse>
    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokedez(@Path("name") pokedexName: String): Response<PokedexDatailDataResponse>
}