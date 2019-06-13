package com.bonobostudios.koklin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
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
        //var DocumentReference = db.collection("users").document("I36wnJankUy2WEb63H7N")

        /*DocumentReference.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "League of")
                Nombre.setText("${document.data}")
            } else {
                Log.d(TAG, "Legends")
            }

        }.addOnFailureListener { e ->
            Log.d(TAG, "No estamos en vivo :(", e)
        }*/


        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var DocumentReference = db.collection("users").document(document.id)
                        DocumentReference.get().addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d(TAG, "League of")
                                Log.d(TAG,document.id)
                                Nombre.setText("${document.data?.getValue("name")}")
                            } else {
                                Log.d(TAG, "Legends")
                            }

                        }.addOnFailureListener { e ->
                            Log.d(TAG, "No estamos en vivo :(", e)
                        }

                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }

    }
}
