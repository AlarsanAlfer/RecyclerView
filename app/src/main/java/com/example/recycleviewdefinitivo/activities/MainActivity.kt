package com.example.recycleviewdefinitivo.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewdefinitivo.provider.PeliculaProvider
import com.example.recycleviewdefinitivo.R
import com.example.recycleviewdefinitivo.adapter.Adaptador
import com.example.recycleviewdefinitivo.dao.PeliculaDAO
import com.example.recycleviewdefinitivo.databinding.ActivityMainBinding
import com.example.recycleviewdefinitivo.model.Pelicula
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listaPelis: MutableList<Pelicula>
    private lateinit var adapter: Adaptador
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var intentLaunch: ActivityResultLauncher<Intent>
    private lateinit var miDAO: PeliculaDAO
    private var listaVacia=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.swipe)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        miDAO = PeliculaDAO()
        listaPelis = miDAO.cargarLista(this)
        layoutManager = LinearLayoutManager(this)
        binding.rvpelis.layoutManager = layoutManager
        adapter = Adaptador(
            listaPelis,
            onClickListener = { peli -> onItemSelected(peli) },
            onItemDoubleClicked = { peli -> openDetallesActivity(peli) }
        )
        binding.rvpelis.adapter = adapter
        binding.rvpelis.setHasFixedSize(true)
        binding.rvpelis.itemAnimator = DefaultItemAnimator()
        setupSwipeRefresh()
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0)
                return true
            }
        })

        intentLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val nuevoNombre = result.data?.getStringExtra("Nombre").toString()
                val posicion = result.data?.getIntExtra("Posicion", -1)

                miDAO.editar(this, listaPelis[posicion!!], nuevoNombre)
                listaPelis[posicion!!].nombre = nuevoNombre
                adapter.notifyItemChanged(posicion)
                binding.rvpelis.adapter = adapter
                display("Se ha actualizado el nombre a $nuevoNombre")
            }
        }

    }

    private fun openDetallesActivity(peli: Pelicula) {
        val intent = Intent(this, Detalles::class.java)
        intent.putExtra("nombre", peli.nombre)
        intent.putExtra("duracion", peli.duracion)
        intent.putExtra("anio", peli.anio)
        intent.putExtra("portada", peli.img)
        intent.putExtra("descripcion", peli.descripcion)
        intent.putExtra("pais", peli.pais)
        startActivity(intent)
    }

    private fun filterList(p0: String?) {
        if (p0 != null) {
            var filteredList = mutableListOf<Pelicula>()
            if (p0.isNotEmpty() && !listaVacia) {
                listaPelis = miDAO.cargarLista(this)
                for (i in listaPelis) {
                    if (i.nombre.lowercase(Locale.ROOT).contains(p0.lowercase())) {
                        filteredList.add(i)
                    }
                }
            } else if (listaPelis.size > 0) {
                filteredList = miDAO.cargarLista(this)
            }

            if (filteredList.isEmpty()) {
                if (p0.isNotEmpty()) {
                    Toast.makeText(this, "No existe esa Pelicula", Toast.LENGTH_SHORT).show()
                } else if (!listaVacia) {
                    filteredList = miDAO.cargarLista(this)
                }

                adapter.setFilteredList(filteredList)
                listaPelis = filteredList
                binding.rvpelis.adapter = Adaptador(
                    pelisLista = listaPelis,
                    onClickListener = { pelicula -> onItemSelected(pelicula) },
                    onItemDoubleClicked = { pelicula -> openDetallesActivity(pelicula) }
                )
            } else {
                adapter.setFilteredList(filteredList)
                listaPelis = filteredList
                binding.rvpelis.adapter = Adaptador(
                    pelisLista = listaPelis,
                    onClickListener = { pelicula -> onItemSelected(pelicula) },
                    onItemDoubleClicked = { pelicula -> openDetallesActivity(pelicula) }
                )
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val peliSelec: Pelicula = listaPelis[item.groupId]
        when (item.itemId) {
            0 -> {
                val alert =
                    AlertDialog.Builder(this).setTitle("Eliminar ${peliSelec.nombre}")
                        .setMessage("¿Estás seguro de que quieres eliminar ${peliSelec.nombre}?")
                        .setNeutralButton("Cerrar", null).setPositiveButton("Aceptar")
                        { _, _ ->
                            display("Se ha eliminado ${peliSelec.nombre}")
                            listaPelis.removeAt(item.groupId)
                            adapter.notifyItemRemoved(item.groupId)
                            adapter.notifyItemRangeChanged(item.groupId, listaPelis.size)
                            binding.rvpelis.adapter

                        }.create()
                alert.show()
            }

            1 -> {
                val intent = Intent(this, Editar_pelicula::class.java)
                intent.putExtra("Portada", peliSelec.img)
                intent.putExtra("Nombre", peliSelec.nombre)
                intent.putExtra("Posicion", item.groupId)
                intentLaunch.launch(intent)
            }

            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menusito, menu)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.eliminar -> {
                listaPelis.clear()
                adapter.notifyDataSetChanged()
                display("Se han eliminado todas las películas")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSwipeRefresh() {
        binding.swipe.setOnRefreshListener {
            listaPelis.clear()
            listaPelis.addAll(miDAO.cargarLista(this))
            //adapter.notifyItemRangeChanged(0, listaPelis.size - 1)
            adapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false
        }
    }

    private fun cargarlista(): MutableList<Pelicula> {
        val lista = mutableListOf<Pelicula>()
        for (pelicula in PeliculaProvider.listaCarga) {
            lista.add(pelicula)
        }
        return lista
    }
    private fun display(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun onItemSelected(peli: Pelicula) {
        Toast.makeText(
            this,
            "Duración: ${peli.duracion} minutos - Año: ${peli.anio}",
            Toast.LENGTH_SHORT
        ).show()
    }

}

