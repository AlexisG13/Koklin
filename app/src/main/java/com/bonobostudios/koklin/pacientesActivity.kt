package com.bonobostudios.koklin

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.koklin.Adapter.pacientesAdapter
import com.bonobostudios.koklin.POJOS.paciente
import kotlinx.android.synthetic.main.pacientes_activity.*

class pacientesActivity: AppCompatActivity(){

    private lateinit var  pacienteAdapter : pacientesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var pacienteList:ArrayList<paciente> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pacientes_activity)


        initRecyclerView()



    }

    fun initRecyclerView(){
        viewManager = LinearLayoutManager(this)
        pacienteAdapter = pacientesAdapter(pacienteList,{pacienteIte: paciente-> pacienteIteClicked(pacienteIte)})

        rv_pacientes.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = pacienteAdapter
        }

    }

    private fun pacienteIteClicked (item: paciente){
        val pacienteBundle=Bundle()
        pacienteBundle.putParcelable("PACIENTE",item)
        //
    }





}