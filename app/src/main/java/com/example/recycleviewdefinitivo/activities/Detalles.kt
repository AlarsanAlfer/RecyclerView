package com.example.recycleviewdefinitivo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recycleviewdefinitivo.R
import com.example.recycleviewdefinitivo.databinding.ActivityDetallesBinding
import com.example.recycleviewdefinitivo.databinding.ActivityEditarPeliculaBinding

class Detalles : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalles)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombre = intent.getStringExtra("nombre")
        val duracion = intent.getIntExtra("duracion", 0)
        val anio = intent.getIntExtra("anio", 0)
        val portada = intent.getIntExtra("portada", 0)
        val descripcion = intent.getStringExtra("descripcion")
        val nacionalidad = intent.getStringExtra("pais")

        binding.tituloP.text = nombre
        binding.duracionP.text = "Duración: $duracion minutos"
        binding.anioP.text = "Año: $anio"
        binding.img.setImageResource(portada)
        binding.descripcionP.text = descripcion
        binding.textView8.text = "País de origen: $nacionalidad"


    }
}