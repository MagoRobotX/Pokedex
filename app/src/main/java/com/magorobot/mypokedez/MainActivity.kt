package com.magorobot.mypokedez

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.magorobot.mypokedez.pokedez.PokedezListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnPokedez = findViewById<Button>(R.id.btnPokedez)

        btnPokedez.setOnClickListener { PokedezListActivity() }

    }

    private fun PokedezListActivity() {
        val intent = Intent(this, PokedezListActivity::class.java)
        startActivity(intent)
    }

}
