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

// Definición de la clase PokedexViewHolder que hereda de RecyclerView.ViewHolder
class PokedexViewHolder(view: View
): RecyclerView.ViewHolder(view) {
    // Vincula el layout del item usando View Binding
    private val binding = ItemPokedezBinding.bind(view)
    // Obtiene referencia al ImageView del layout
    private val imageView: ImageView = itemView.findViewById(R.id.ivPokeImag)
    // Función para vincular los datos del Pokémon al ViewHolder
    fun bind(PokedexItemResponse: PokedexItemResponse, onItemSelected: (String) -> Unit) {
        // Asigna el nombre del Pokémon al TextView correspondiente
        binding.tvPokedezName.text = PokedexItemResponse.namepokemon
     // Construye la URL de la imagen del Pokémon usando su ID
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${PokedexItemResponse.id}.png"

        // Cargar la imagen con Picasso en la lista de pokemon
        Picasso.get()
            .load(imageUrl)
            .into(imageView)

        // Configurar el listener para la selección del ítem completo
        itemView.setOnClickListener {
            onItemSelected(PokedexItemResponse.namepokemon)
        }
         // Configura otro listener de clic para la raíz del binding
        // (posiblemente redundante con el anterior)
        binding.root.setOnClickListener { onItemSelected(PokedexItemResponse.namepokemon) }
    }

}
