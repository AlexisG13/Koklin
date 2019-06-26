package com.bonobostudios.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.POJOS.paciente
import com.bonobostudios.koklin.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.pacientes_cardview.view.*

open class PacienteAdapter (query: Query,private val listener: OnPacienteSelectedListener):FirestoreAdapter<PacienteAdapter.ViewHolder>(query){

    interface OnPacienteSelectedListener{
        fun onPacienteSelected(paciente: DocumentSnapshot)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.pacientes_cardview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnPacienteSelectedListener?
        ){

            val paciente = snapshot.toObject(paciente::class.java)
            if (paciente==null){
                return
            }

            val resources = itemView.resources

            itemView.tv_nombrePaciente.text=paciente.nombre
            itemView.setOnClickListener {
                listener?.onPacienteSelected(snapshot)
            }

        }
    }

}