package com.bonobostudios.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.POJOS.evaluacion
import com.bonobostudios.koklin.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.evaluacion_cardview.view.*

open class EvaluacionAdapter (query: Query, private val listener: OnEvaluacionSelectedListener) :
    FirestoreAdapter<EvaluacionAdapter.ViewHolder>(query){

    interface OnEvaluacionSelectedListener{
        fun onEvaluacionSelected(evaluacion: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.evaluacion_cardview , parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind (
            snapshot: DocumentSnapshot,
            listener: OnEvaluacionSelectedListener?

            ){
            val evaluacion = snapshot.toObject(evaluacion::class.java) ?: return

            val resources = itemView.resources
           // itemView.tv_fecha.text=evaluacion.FECHA
            itemView.puntaje.text=evaluacion.score.toString()

            itemView.fechaEVa.text=evaluacion.fecha

            itemView.setOnClickListener {
                listener?.onEvaluacionSelected(snapshot)
            }

        }
    }

}