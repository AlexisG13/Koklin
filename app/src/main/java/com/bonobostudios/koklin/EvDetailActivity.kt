package com.bonobostudios.koklin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.respuestas_activity.*

class EvDetailActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.respuestas_activity)

        var intent= intent
        //var usuario = intent.getStringExtra("USER_ID")
        var paciente = intent.getStringExtra("EVALUACION_ID")

        BtnAceptarResultados.setOnClickListener {
            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        read(paciente)
    }

    fun read(id:String){
        val datos = db.collection("evaluaciones ").document(id)
        datos.get().addOnSuccessListener {document->
            if(document!=null){

            }else{

            }

        }
    }

}