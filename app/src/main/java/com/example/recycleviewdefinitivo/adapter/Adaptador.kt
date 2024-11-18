package com.example.recycleviewdefinitivo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewdefinitivo.model.Pelicula
import com.example.recycleviewdefinitivo.R

class Adaptador(private var pelisLista:List<Pelicula>, private var onClickListener:(Pelicula)->Unit, private val onItemDoubleClicked: (Pelicula) -> Unit) : RecyclerView.Adapter<PelisViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelisViewHolder {
        var layoutInflater=LayoutInflater.from(parent.context)
        return PelisViewHolder(layoutInflater.inflate(R.layout.targeta_peli,parent,false))
    }

    override fun onBindViewHolder(holder: PelisViewHolder, position: Int) {
        var item=pelisLista[position]
        holder.render(item, onClickListener, onItemDoubleClicked)
    }

    override fun getItemCount(): Int {
        return pelisLista.size
    }

    fun setFilteredList(mList: MutableList<Pelicula>){
        notifyItemRangeRemoved(0,mList.size)
        this.pelisLista=mList
        notifyItemRangeInserted(0,mList.size)
    }

}