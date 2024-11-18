package com.example.recycleviewdefinitivo.contract

import android.provider.BaseColumns

class PeliculaContract {
    companion object{
        val NOMBRE_BD = "dbpeliculas"
        val VERSION = 1
        class Entrada: BaseColumns {
            companion object{
                val TABLA = "peliculas"
                val IDCOL = "id"
                val NOMBRECOL = "nombre"
                val DESCRIPCIONCOL = "descripcion"
                val IMAGENCOL = "imagen"
                val DURACIONCOL = "duración"
                val ANIOCOL = "año"
                val PAISCOL = "pais"
            }
        }
    }
}