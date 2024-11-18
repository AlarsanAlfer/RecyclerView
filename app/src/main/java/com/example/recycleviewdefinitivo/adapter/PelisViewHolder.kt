package com.example.recycleviewdefinitivo.adapter

import android.view.ContextMenu
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.recycleviewdefinitivo.model.Pelicula
import com.example.recycleviewdefinitivo.databinding.TargetaPeliBinding
import com.squareup.picasso.Picasso

class PelisViewHolder(view: View) : ViewHolder(view), View.OnCreateContextMenuListener {

    val binding = TargetaPeliBinding.bind(view)
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300

    fun render(item: Pelicula, onClickListener: (Pelicula) -> Unit, onItemDoubleClicked: (Pelicula) -> Unit) {
        binding.nombre.text = item.nombre
        binding.foto.setImageResource(item.img)

        Picasso.get().load(item.img).fit().into(binding.foto)

        itemView.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onItemDoubleClicked(item)
            } else {
                onClickListener(item)
            }
            lastClickTime = clickTime
        }
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0!!.setHeaderTitle(binding.nombre.text)
        p0.add(this.adapterPosition, 0, 0, "Eliminar")
        p0.add(this.adapterPosition, 1, 0, "Editar")
    }


}

