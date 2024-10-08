package com.magorobot.mypokedez.pokedez

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Obtiene la lista de Pokémones según un nombre
    @GET("pokemon/{pokemon}")
    suspend fun getPokedex(@Path("pokemon") pokedexName: String): Response<PokedexDataResponse>
    // para la imagen y  las estadisticas
    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokedez(@Path("name") pokedexName: String): Response<PokedexDatailDataResponse>

}