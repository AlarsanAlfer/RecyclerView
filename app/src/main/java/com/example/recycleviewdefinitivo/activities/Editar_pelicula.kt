package com.example.recycleviewdefinitivo.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recycleviewdefinitivo.R
import com.example.recycleviewdefinitivo.databinding.ActivityEditarPeliculaBinding

class Editar_pelicula : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPeliculaBinding

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditarPeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombre = intent.getStringExtra("Nombre")
        val img = intent.getIntExtra("Portada", 0)
        val posicion = intent.getIntExtra("Posicion", 0)

        binding.editText.hint=nombre
        binding.img.setImageResource(img)
        binding.cancelarBtn.setOnClickListener{
            finish()
        }
        binding.cambiarBtn.setOnClickListener{
            val nuevoNombre = binding.editText.text.toString()
            val intent = Intent()
                intent.putExtra("Nombre", nuevoNombre)
                intent.putExtra("Posicion", posicion)
            setResult(RESULT_OK, intent)
            finish()
        }


    }

}