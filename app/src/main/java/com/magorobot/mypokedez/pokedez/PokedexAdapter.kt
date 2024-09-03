package com.magorobot.mypokedez.pokedez

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.magorobot.mypokedez.R
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Response

class PokedexAdapter(

    var PokedexList: List<PokedexItemResponse> = emptyList(),
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<PokedexViewHolder>() {
    fun updaterList(PokedexList: List<PokedexItemResponse>) {
        this.PokedexList = PokedexList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokedexViewHolder {

        return PokedexViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokedez, parent, false),

        )
    }

    override fun onBindViewHolder(viewholder: PokedexViewHolder, position: Int) {
        //val  item= PokedexList[position]
        viewholder.bind(PokedexList[position],onItemSelected)

    }

    override fun getItemCount() = PokedexList.size

}

