package com.bonobostudios.koklin.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.koklin.POJOS.paciente
import com.bonobostudios.koklin.R
import kotlinx.android.synthetic.main.pacientes_cardview.view.*

class pacientesAdapter (var pacientes : List<paciente>,val clickListener:(paciente)->Unit):RecyclerView.Adapter<pacientesAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pacientes_cardview,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int)= holder.bind(pacientes[position],clickListener)


    override fun getItemCount() = pacientes.size

    fun changeList(pacientes:List<paciente>){
        this.pacientes = pacientes
        notifyDataSetChanged()
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(item: paciente,clickListener: (paciente) -> Unit)=with(itemView){
            tv_nombrePaciente.text=item.Nombre
            this.setOnClickListener { clickListener(item) }
        }
    }

}