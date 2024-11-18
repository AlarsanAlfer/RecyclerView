package com.example.recycleviewdefinitivo.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.recycleviewdefinitivo.contract.PeliculaContract
import com.example.recycleviewdefinitivo.model.Pelicula

class PeliculaDAO {
    fun cargarLista(context: Context?): MutableList<Pelicula>{
        lateinit var res:MutableList<Pelicula>
        lateinit var c: Cursor
        try{

            val bd = DBOpenHelper.getInstance(context)!!.readableDatabase
            // val sql = "SELECT * FROM frutas;"
            // c = db.rawQuery(sql, null)
            val columnas = arrayOf(
                PeliculaContract.Companion.Entrada.IDCOL,
                PeliculaContract.Companion.Entrada.NOMBRECOL,
                PeliculaContract.Companion.Entrada.DESCRIPCIONCOL,
                PeliculaContract.Companion.Entrada.IMAGENCOL,
                PeliculaContract.Companion.Entrada.DURACIONCOL,
                PeliculaContract.Companion.Entrada.ANIOCOL,
                PeliculaContract.Companion.Entrada.PAISCOL)

            c = bd.query(PeliculaContract.Companion.Entrada.TABLA,
                columnas,null,null,null,null,null)

            res= mutableListOf()

            while (c.moveToNext()) {
                val nueva = Pelicula(c.getInt(0),c.getString(1),
                    c.getString(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getString(6))
                res.add(nueva)
            }
        }finally {
            c.close()
        }
        return res
    }

    fun actualizarBBDD(context: Context?, pelicula: Pelicula) {
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        /*db.execSQL(
            "UPDATE peliculas "
                    + "SET nombre='${pelicula.nombre}' " +
                    "SET descripcion='${pelicula.descripcion}'" +
                    "SET imagen='${pelicula.imagen}'" +
                    "WHERE id=${pelicula.id};"                añadir el resto de atributos.
        )
        */
        val values = ContentValues()
        values.put(PeliculaContract.Companion.Entrada.IDCOL,pelicula.id)
        values.put(PeliculaContract.Companion.Entrada.NOMBRECOL,pelicula.nombre)
        values.put(PeliculaContract.Companion.Entrada.DESCRIPCIONCOL,pelicula.descripcion)
        values.put(PeliculaContract.Companion.Entrada.IMAGENCOL,pelicula.img)
        values.put(PeliculaContract.Companion.Entrada.DURACIONCOL, pelicula.duracion)
        values.put(PeliculaContract.Companion.Entrada.ANIOCOL, pelicula.anio)
        values.put(PeliculaContract.Companion.Entrada.PAISCOL, pelicula.pais)
        db.update(PeliculaContract.Companion.Entrada.TABLA,values,"id=?",arrayOf(pelicula.id.toString()))
        db.close()
    }
    fun eliminar(context: Context?, pelicula: Pelicula){
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        db.execSQL(
            "DELETE FROM frutas WHERE id= ${pelicula.id};"
        )

        /* val values = arrayOf((pelicula.id).toString())
         db.delete(PeliculaContract.Companion.Entrada.NOMBRE_TABLA,"id=?",values)*/
        db.close()
    }

    fun editar(context: Context?, pelicula: Pelicula, nuevoTitulo: String){
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        val values = ContentValues()
        values.put(PeliculaContract.Companion.Entrada.NOMBRECOL, nuevoTitulo)
        db.update(PeliculaContract.Companion.Entrada.TABLA,values,"id=?",arrayOf(pelicula.id.toString()))
        db.close()
    }




}