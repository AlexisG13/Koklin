package com.bonobostudios.koklin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.respuestas_activity.*


private var fecha=""
private var paciente=""
private var score=""
private var malas=0

class EvDetailActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.respuestas_activity)

        val intent= intent
        //var usuario = intent.getStringExtra("USER_ID")
        val paciente = intent.getStringExtra("EVALUACION_ID")

        BtnAceptarResultados.setOnClickListener {
            val intent  = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        read(paciente)
    }

    fun read(id:String){
        val datos = db.collection("evaluaciones ").document(id)
        datos.get().addOnSuccessListener {document->
            if(document!=null){
                fecha=document.get("fecha").toString()
                score=document.get("score").toString()
                paciente=document.get("paciente").toString()
                malas=10- score.toInt()
                var Res = ""
                if(score.toInt()<=4) {
                    Res =  "Audicion baja"
                }
                else if(score.toInt()>=5&&score.toInt()<=8){
                    Res = "Audicion media"
                }
                else if(score.toInt()>=9){
                    Res = "Audicion buena"
                }
                NombreExaminado.text= paciente
                NumeroDeBuenas.text= score
                ResultadoFinal.text= Res
                Fecha.text = fecha
                NumeroDeMalas.text= malas.toString()



            }else{

            }

        }
    }

}