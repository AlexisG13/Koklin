package com.bonobostudios.koklin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonobostudios.koklin.POJOS.paciente

class detallePacientes: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reciever: paciente= intent?.extras?.getParcelable("PACIENTE") ?: paciente()

        init(reciever)
    }

    fun init(Paciente:paciente){

    }




}

