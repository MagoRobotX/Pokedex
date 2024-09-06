package com.magorobot.mypokedez.pokedez

import com.google.gson.annotations.SerializedName


/// Esta es la clase que representa la respuesta completa del detalle del Pokémon
data class PokedexDatailDataResponse(
    @SerializedName("name") val diname: String,
    @SerializedName("sprites") val sprites: sprites, //optengo la imagen
    @SerializedName("stats") val pokedez: List<PokedexStatistics> //las estadisticas de atake

)

// Esta clase representa cada estadística (stat) del Pokémon
data class PokedexStatistics(
     @SerializedName("base_stat") val baseStats: String, // el vivel de poder
    @SerializedName("stat") val stat: StatNamePOder // entro ala lista de estadisticas

)

// Esta clase representa los detalles del nombre de la estadística
data class StatNamePOder(
    @SerializedName("name") val name: String// nombre de la estadistica
)

//url de la imagen
data class  sprites( @SerializedName ("back_default") val url: String)