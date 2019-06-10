package com.bonobostudios.koklin

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val TAG = ""

    val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        et4.setOnClickListener {
            var nombre = et1.text.toString()

            var mail = et2.text.toString()

            var password = et3.text.toString()

            val user = hashMapOf(
                "email" to mail,
                "name" to nombre,
                "password" to password
            )
            db.collection("users").add(user)
        }

    }
}
