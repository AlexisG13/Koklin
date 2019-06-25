package com.bonobostudios.koklin.Adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.koklin.POJOS.paciente
import com.bonobostudios.koklin.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.pacientes_cardview.view.*
/*


class pacientesAdapter (options : FirestoreRecyclerOptions<paciente>): FirestoreRecyclerAdapter<paciente,pacientesAdapter.ViewHolder>(options){

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: paciente) {
        holder.bind(model)
    }
    private val TAG: String = "Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.activity_main, parent, false));
    }
    override fun onDataChanged() {
        super.onDataChanged()
        Log.v(TAG, "onDataChanged")
    }

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(model: paciente){
        itemView.tv_nombrePaciente.text=model.getName()

    }
}


}
*/
