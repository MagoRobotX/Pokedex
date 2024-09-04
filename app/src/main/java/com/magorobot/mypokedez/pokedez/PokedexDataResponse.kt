package com.magorobot.mypokedez.pokedez

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class PokedexDataResponse(
    @SerializedName("results") val pokedex: List<PokedexItemResponse>
)

data class PokedexItemResponse(
    @SerializedName("name") val namepokemon: String,
    @SerializedName("url") val url: String
) {
    // Extraer el ID del Pokémon desde la URL
    val id: Int
        get() = url.split("/".toRegex()).dropLast(1).last().toInt()
}
/**
data class spriterResponse(
    @SerializedName("sprites") val SpriImg: PokeImagenResponse
)
data class PokeImagenResponse(@SerializedName("back_default") val imgUrl:String)
**/
