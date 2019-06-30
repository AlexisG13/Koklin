package com.bonobostudios.koklin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*


private var name = ""
private var email=""
class ProfileActivity : AppCompatActivity() {
    val rootRef = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var us=FirebaseAuth.getInstance().currentUser?.uid
        var datos= rootRef.collection("users").document(us!!)
        datos.get().addOnSuccessListener {document->
            if(document!=null){


                name=document.get("name").toString()
                email=document.get("email").toString()

                NombreEspecialista.text= name
                CargoEspecialista.text= email




            }else{

            }


        }
        btn_sign_out.setOnClickListener{
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    var mIntent = Intent(this,LoginActivity::class.java)
                    mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mIntent)
                    finish()
                }
        }

        MyProfileView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
