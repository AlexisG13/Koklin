package com.bonobostudios.koklin.Adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.POJOS.paciente
import com.bonobostudios.koklin.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

import kotlinx.android.synthetic.main.pacientes_cardview.view.*
  class PacienteAdapterFirestore  constructor(options: FirestoreRecyclerOptions <paciente>) : FirestoreRecyclerAdapter <paciente, PacienteViewHolder>(options){

    override fun onBindViewHolder(holder:PacienteViewHolder, position: Int, PA: paciente) {
        holder.setPacienteName(PA.nombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.pacientes_cardview,parent,false)
        return PacienteViewHolder(view)
    }

}

 class PacienteViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view){
    internal fun setPacienteName(pacienteName:String){
        val textView = view.findViewById<TextView>(R.id.tv_nombrePaciente)
        textView.text=pacienteName
    }

}


