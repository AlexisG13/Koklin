package com.bonobostudios.koklin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private val id = "jkvlY3trCOuvfl9N56HF"
    val TAG="XD: "
    var fecha=""
    var respuesta=""
    var autor=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        read()
    }

    fun read(){
        val datos=db.collection("evaluaciones ").document(id)
        datos.get()
            .addOnSuccessListener {document ->

                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    fecha=document.get("FECHA").toString()
                    Log.d(TAG,fecha)
                    tv_fechaEVA.text = fecha



                } else {
                    Log.d(TAG, "No such document")
                }



            }

    }


}
