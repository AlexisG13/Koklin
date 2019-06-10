package com.bonobostudios.koklin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    val TAG = ""

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Read()

    }

    fun Read() {

        //var algo = db.collection("users").document().id
        var DocumentReference = db.collection("users").document("I36wnJankUy2WEb63H7N")

        DocumentReference.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "League of")
                Nombre.setText("${document.data}")
            } else {
                Log.d(TAG, "Legends")
            }

        }.addOnFailureListener { e ->
            Log.d(TAG, "No estamos en vivo :(", e)
        }
    }
}
