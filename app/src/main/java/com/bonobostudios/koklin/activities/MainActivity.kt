package com.bonobostudios.koklin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bonobostudios.koklin.R
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
                        val L1Reference = db.collection("nivel").document("L1")
                            .collection("preguntas").document("pregunta1")
                        L1Reference.get().addOnSuccessListener { document ->
                            val pregunta = document.get("pregunta").toString()
                            for (actual in 1..4) {
                                val respuestasReference =
                                    L1Reference.collection("respuestas").document("respuesta$actual")
                                respuestasReference.get().addOnSuccessListener { document ->
                                    val respuesta = document.get("respuesta").toString()
                                    val esCorrecta = document.get("esCorrecta").toString()
                                    if (document != null) {
                                        PreguntaID.setText(pregunta)

                                        if ("respuesta$actual" == "respuesta1") {
                                            respuesta1.setText(respuesta)
                                        } else if ("respuesta$actual" == "respuesta2") {
                                            respuesta2.setText(respuesta)
                                        } else if ("respuesta$actual" == "respuesta3") {
                                            respuesta3.setText(respuesta)
                                        } else if ("respuesta$actual" == "respuesta4") {
                                            respuesta4.setText(respuesta)
                                        }
                                    } else {
                                        Log.w("ERROR!", "Algo pasó porque no estamos en vivo.")
                                    }
                                }
                            }
                        }
                    } else {
                        Log.w("WARNING!", "No se pudo encontrar nada que cargar.", task.exception)
                    }
                }
            }
    }
}
