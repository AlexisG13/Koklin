package com.bonobostudios.koklin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_patient_info.*

class nuevoPaciente: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)

        val db = FirebaseFirestore.getInstance()

        BtnAceptar.setOnClickListener {
            var nombre = etNombrePaciente.text.toString()
            var edad = etNombrePaciente.text.toString()

            val pacient = hashMapOf(
                "Nombre" to nombre,
                "Edad" to edad
            )

            db.collection("pacientes").add(pacient)

        }

    }
}