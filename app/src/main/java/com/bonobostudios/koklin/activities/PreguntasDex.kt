package com.bonobostudios.koklin.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bonobostudios.koklin.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = ""

    val db = FirebaseFirestore.getInstance()
    var intentos = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ReadAllQuestions()
    }


    private fun ReadAllQuestions() {
        db.collection("nivel")
            .get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    if (task.isSuccessful) {
                        val pReference = db.collection("nivel").document("L1")
                            .collection("preguntas").document("pregunta1")
                        ReadAllAnswers(pReference, 1)

                    } else {
                        Log.w("WARNING!", "No se pudo encontrar nada que cargar.", task.exception)
                    }
                }
            }
    }

    private fun ReadAllAnswers(reference: DocumentReference, pActual: Int) {
        reference.get().addOnSuccessListener { document ->
            val pregunta = document.get("pregunta").toString()
            for (actual in 1..4) {
                val respuestasReference =
                    reference.collection("respuestas").document("respuesta$actual")
                respuestasReference.get().addOnSuccessListener { document ->
                    val respuesta = document.get("respuesta").toString()
                    val esCorrecta = document.get("esCorrecta").toString()
                    if (document != null) {
                        PreguntaID.setText(pregunta)
                        if ("respuesta$actual" == "respuesta1") {
                            loadThings(respuesta1,respuesta,esCorrecta,pActual)
                        } else if ("respuesta$actual" == "respuesta2") {
                            loadThings(respuesta2,respuesta,esCorrecta,pActual)
                        } else if ("respuesta$actual" == "respuesta3") {
                            loadThings(respuesta3,respuesta,esCorrecta,pActual)
                        } else if ("respuesta$actual" == "respuesta4") {
                            loadThings(respuesta4,respuesta,esCorrecta,pActual)
                        }
                    } else {
                        Log.w("ERROR!", "Algo pasó porque no estamos en vivo.")
                    }
                }
                respuesta1.setBackgroundColor(Color.argb(0, 0, 0, 0))
                respuesta2.setBackgroundColor(Color.argb(0, 0, 0, 0))
                respuesta3.setBackgroundColor(Color.argb(0, 0, 0, 0))
                respuesta4.setBackgroundColor(Color.argb(0, 0, 0, 0))
            }
        }
    }

    private fun loadThings(context : TextView, respuesta:String,esCorrecta:String,pActual: Int){
        context.setText(respuesta)
        context.setOnClickListener {
            //var intentosDeX = intentos
            if (esCorrecta == "true") {
                var pNueva = pActual + 1
                context.setBackgroundColor(Color.argb(255, 41, 181, 48))
                val pReference = db.collection("nivel").document("L1")
                    .collection("preguntas").document("pregunta$pNueva")
                if (pNueva > 3) {
                    Toast.makeText(this, "¡Has completado el nivel!", Toast.LENGTH_LONG).show()
                } else {
                    ReadAllAnswers(pReference, pNueva)
                }
            } else {
                context.setBackgroundColor(Color.argb(255, 181, 41, 48))
                //intentosDeX=intentosDeX-1
                intentos--
                if (intentos >= 0) {
                    IntentosRestantes.setText(intentos.toString())
                }
                if (intentos == 0) {
                    PreguntaID.setText("GAME OVER")
                    Toast.makeText(this, "¡Perdiste!", Toast.LENGTH_LONG).show()
                    if (esCorrecta == "true") {
                        context.setBackgroundColor(Color.argb(255, 41, 181, 48))
                    }
                }
            }
        }
    }
}
