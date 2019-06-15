package com.bonobostudios.koklin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bonobostudios.koklin.R
import com.google.firebase.firestore.FirebaseFirestore

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
                                    if (document != null) {
                                        Log.d("PREGUNTA ", pregunta)
                                        Log.d("RESPUESTA ", respuesta)
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
