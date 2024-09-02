package com.magorobot.mypokedez.pokedez

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.magorobot.mypokedez.R
import com.magorobot.mypokedez.databinding.ItemPokedezBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PokedexViewHolder(view: View, private val onItemSelected: (String) -> Unit
): RecyclerView.ViewHolder(view) {
    private val binding = ItemPokedezBinding.bind(view)
    private val imageView: ImageView = itemView.findViewById(R.id.ivPokeImag)
    fun bind(PokedexItemResponse: PokedexItemResponse) {
        // Asignar el nombre del Pokémon
        binding.tvPokedezName.text = PokedexItemResponse.namepokemon
    // Construir la URL correcta usando el ID del Pokémon
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${PokedexItemResponse.id}.png"

        // Cargar la imagen con Picasso
        Picasso.get()
            .load(imageUrl)
            .into(imageView)

        // Configurar el listener para la selección del ítem
        itemView.setOnClickListener {
            onItemSelected(PokedexItemResponse.namepokemon)
        }
    }

}
