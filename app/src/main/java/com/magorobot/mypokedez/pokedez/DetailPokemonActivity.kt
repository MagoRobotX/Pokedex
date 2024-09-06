package com.magorobot.mypokedez.pokedez

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.magorobot.mypokedez.R
import com.magorobot.mypokedez.databinding.ActivityDetailPokemonBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.suspendCoroutine

// Definición de la clase DetailPokemonActivity que hereda de AppCompatActivity
class DetailPokemonActivity : AppCompatActivity() {
    // Objeto compañero que contiene constantes para la clase
    companion object {
        // Constante para la clave del extra que se pasa en el Intent
        const val EXTRA_ID = "extra_id"
    }

    // Declaración de una propiedad lateinit para el binding de la vista
    private lateinit var binding: ActivityDetailPokemonBinding

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        // Llama al método onCreate de la superclase
        super.onCreate(savedInstanceState)
        // Infla el layout y lo asigna a la propiedad binding
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        // Establece la vista de contenido de la actividad
        setContentView(binding.root)
        // Obtiene el ID del Pokémon del Intent, si no existe, usa una cadena vacía
        val id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        // Llama a la función para obtener la información del Pokémon
        getPokedexInformation(id)
    }


     // Función para obtener la información del Pokémon de la API
    private fun getPokedexInformation(id: String) {
        // Lanza una corrutina en el hilo de IO
        CoroutineScope(Dispatchers.IO).launch {
            // Realiza la llamada a la API
            val detail = getRetrofit().create(ApiService::class.java).getPokedez(id)
            // Si la respuesta del cuerpo no es nula
            if (detail.body() != null) {
                // Ejecuta la creación de la UI en el hilo principal
                runOnUiThread { createUI(detail.body()!!) }
            }
        }
    }
    // Función para crear la interfaz de usuario con los datos del Pokémon
    private fun createUI(poke: PokedexDatailDataResponse) {
        // Cargar la imagen del Pokémon con Picasso
        Picasso.get()
            .load(poke.sprites.url)
            .into(binding.ivPokeImagen)
        // Mostrar el nombre del Pokémon
        binding.tvPokeName.text = poke.diname


        // Variables para almacenar las estadisticas HP, ATK, Speed y Defense
        var hpStat: Int = 0
        var atkStat: Int = 0
        var speed: Int = 0
        var defense: Int = 0

        // Repie varias veces los procesas sobre las estadísticas para encontrar HP, ATK, Speed y Defense
        for (stat in poke.pokedez) {
            when (stat.stat.name) {
                "hp" -> hpStat = stat.baseStats.toIntOrNull() ?: 0
                "attack" -> atkStat = stat.baseStats.toIntOrNull() ?: 0
                "speed" -> speed = stat.baseStats.toIntOrNull() ?: 0
                "defense" -> defense = stat.baseStats.toIntOrNull() ?: 0
            }
        }

        // Mostrar HP, ATK, Speed y Defense en la interfaz de usuario
        binding.tvPokeHp.text = hpStat.toString()
        binding.tvAttack.text = atkStat.toString()
        binding.tvDefense.text = defense.toString()
        binding.tvVelocida.text = speed.toString()

        // Ajustar el ancho del View para representar el progreso
        val maxStatValue = 325 // Puedes ajustar este valor según tus necesidades

        val hpWidth = (hpStat / maxStatValue.toFloat() * 1000).toInt()
        val atkWidth = (atkStat / maxStatValue.toFloat() * 1000).toInt()
        val speedWidth = (speed / maxStatValue.toFloat() * 1000).toInt()
        val defenseWidth = (defense / maxStatValue.toFloat() * 1000).toInt()

        // Ajustar el ancho del View (progreso) en píxeles
        binding.viewHp.layoutParams.width = hpWidth.dpToPx(binding.root.context)
        binding.viwAtack.layoutParams.width = atkWidth.dpToPx(binding.root.context)
        binding.viewDef.layoutParams.width = defenseWidth.dpToPx(binding.root.context)
        binding.viewSpee.layoutParams.width = speedWidth.dpToPx(binding.root.context)


        // Solicitar que la vista se vuelva a dibujar
        binding.viewHp.requestLayout()
        binding.viwAtack.requestLayout()
        binding.viewDef.requestLayout()
        binding.viewSpee.requestLayout()

    }

}

// Función de extensión para convertir dimensiones de dp (density-independent pixels) a px (pixels)
private fun Int.dpToPx(context: Context): Int {
    // Obtiene la densidad de pantalla del dispositivo
    val density = context.resources.displayMetrics.density
    // Multiplica el valor en dp por la densidad y lo convierte a un entero para obtener el equivalente en px
    return (this * density).toInt()
}

fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") // Base URL para la API
        .addConverterFactory(GsonConverterFactory.create())// Conversor de JSON a objetos Kotlin
        .build()
}
