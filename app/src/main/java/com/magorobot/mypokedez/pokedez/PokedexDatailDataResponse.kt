package com.magorobot.mypokedez.pokedez

import com.google.gson.annotations.SerializedName


// Esta es la clase que representa la respuesta completa del detalle del Pokémon
data class PokedexDatailDataResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val di: String,
    @SerializedName("sprites") val sprites: sprites,
    @SerializedName("stats") val pokedez: List<PokedexResponse>

)

// Esta clase representa cada estadística (stat) del Pokémon
data class PokedexResponse(
    @SerializedName("base_stat") val baseStats: String,
    @SerializedName("stat") val stat: StatDetail
)

// Esta clase representa los detalles del nombre de la estadística
data class StatDetail(
    @SerializedName("name") val name: String
)

data class  sprites( @SerializedName ("back_default") val url: String)
/**
{
    // Extraer el ID del Pokémon desde la URL
    val id: Int
        get() = url.split("/".toRegex()).dropLast(1).last().toInt()
}
**/