package com.bonobostudios.koklin.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bonobostudios.koklin.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = ""

    val db = FirebaseFirestore.getInstance()

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
                        for (pActual in 1..2) {
                            if ("pregunta$pActual" == "pregunta1" && isComplete(false)) {
                                while (isComplete(false)) {
                                    val pReference = db.collection("nivel").document("L$pActual")
                                        .collection("preguntas").document("pregunta$pActual")
                                    ReadAllAnswers(pReference)
                                }
                            }
                            if ("pregunta$pActual" == "pregunta2" && isComplete(false)) {
                                while (isComplete(false)) {
                                    val pReference = db.collection("nivel").document("L1")
                                        .collection("preguntas").document("pregunta$pActual")
                                    ReadAllAnswers(pReference)
                                }
                            }

                        }
                    } else {
                        Log.w("WARNING!", "No se pudo encontrar nada que cargar.", task.exception)
                    }
                }
            }
    }

    private fun isComplete(completa: Boolean): Boolean {
        if (completa == true) {
            return true
        }
        return false
    }

    private fun ReadAllAnswers(reference: DocumentReference) {
        var intentos = 3
        reference.get().addOnSuccessListener { document ->
            val pregunta = document.get("pregunta").toString()
            val completada = document.get("completada")
            for (actual in 1..4) {
                val respuestasReference =
                    reference.collection("respuestas").document("respuesta$actual")
                respuestasReference.get().addOnSuccessListener { document ->
                    val respuesta = document.get("respuesta").toString()
                    val esCorrecta = document.get("esCorrecta").toString()
                    if (document != null) {
                        PreguntaID.setText(pregunta)

                        if ("respuesta$actual" == "respuesta1") {
                            respuesta1.setText(respuesta)
                            respuesta1.setOnClickListener {
                                if (esCorrecta == "true") {
                                    respuesta1.setBackgroundColor(Color.argb(255, 41, 181, 48))
                                    isComplete(true)

                                } else {
                                    respuesta1.setBackgroundColor(Color.argb(255, 181, 41, 48))
                                    intentos = intentos - 1
                                    IntentosRestantes.setText(intentos.toString())
                                    if (intentos == 0) {
                                        PreguntaID.setText("GAME OVER")
                                        respuesta1.setBackgroundColor(Color.argb(255, 41, 181, 48))

                                    }
                                }
                            }
                        } else if ("respuesta$actual" == "respuesta2") {
                            respuesta2.setText(respuesta)
                            respuesta2.setOnClickListener {
                                if (esCorrecta == "true") {
                                    respuesta2.setBackgroundColor(Color.argb(255, 41, 181, 48))
                                    isComplete(true)

                                } else {
                                    respuesta2.setBackgroundColor(Color.argb(255, 181, 41, 48))
                                    intentos = intentos - 1
                                    IntentosRestantes.setText(intentos.toString())
                                    if (intentos == 0) {
                                        PreguntaID.setText("GAME OVER")
                                        respuesta1.setBackgroundColor(Color.argb(255, 41, 181, 48))

                                    }
                                }
                            }
                        } else if ("respuesta$actual" == "respuesta3") {
                            respuesta3.setText(respuesta)
                            respuesta3.setOnClickListener {
                                if (esCorrecta == "true") {
                                    respuesta3.setBackgroundColor(Color.argb(255, 41, 181, 48))
                                    isComplete(true)

                                } else {
                                    respuesta3.setBackgroundColor(Color.argb(255, 181, 41, 48))
                                    intentos = intentos - 1
                                    IntentosRestantes.setText(intentos.toString())
                                    if (intentos == 0) {
                                        PreguntaID.setText("GAME OVER")
                                        respuesta1.setBackgroundColor(Color.argb(255, 41, 181, 48))

                                    }
                                }
                            }
                        } else if ("respuesta$actual" == "respuesta4") {
                            respuesta4.setText(respuesta)
                            respuesta4.setOnClickListener {
                                if (esCorrecta == "true") {
                                    respuesta4.setBackgroundColor(Color.argb(255, 41, 181, 48))
                                    isComplete(true)
                                } else {
                                    respuesta4.setBackgroundColor(Color.argb(255, 181, 41, 48))
                                    intentos = intentos - 1
                                    IntentosRestantes.setText(intentos.toString())
                                    if (intentos == 0) {
                                        PreguntaID.setText("GAME OVER")
                                        respuesta1.setBackgroundColor(Color.argb(255, 41, 181, 48))

                                    }
                                }
                            }
                        }
                    } else {
                        Log.w("ERROR!", "Algo pasó porque no estamos en vivo.")
                    }
                }
            }
        }
    }
}
