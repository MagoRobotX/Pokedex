package com.magorobot.mypokedez.pokedez

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.magorobot.mypokedez.R
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Response
// Definición de la clase PokedexAdapter que hereda de RecyclerView.Adapter
class PokedexAdapter(
    // Lista de elementos Pokédex, inicializada como una lista vacía
    var PokedexList: List<PokedexItemResponse> = emptyList(),
    // Función lambda que se ejecutará cuando se seleccione un elemento
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<PokedexViewHolder>() {
        // Función para actualizar la lista de elementos Pokédex
    fun updaterList(PokedexList: List<PokedexItemResponse>) {
        this.PokedexList = PokedexList
            // Notifica al RecyclerView que el conjunto de datos ha cambiado
        notifyDataSetChanged()
    }

    // Crea y devuelve un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokedexViewHolder {
    // Infla el layout del item y crea un nuevo PokedexViewHolder
        return PokedexViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokedez, parent, false),

            )
    }
// Vincula los datos a un ViewHolder existente
    override fun onBindViewHolder(viewholder: PokedexViewHolder, position: Int) {
        // Llama al método bind del ViewHolder, pasando el elemento de la lista y la función onItemSelected
        viewholder.bind(PokedexList[position], onItemSelected)

    }
    // Devuelve el número total de elementos en la lista
    override fun getItemCount() = PokedexList.size

}

