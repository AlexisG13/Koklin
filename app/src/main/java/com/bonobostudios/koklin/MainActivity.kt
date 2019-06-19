package com.bonobostudios.koklin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bonobostudios.koklin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import java.util.*


class MainActivity : AppCompatActivity() {


    val db = FirebaseFirestore.getInstance()
    private val REQUEST_CODE = 2019
    val auth = FirebaseAuth.getInstance()

    lateinit var providers : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bonobostudios.koklin.R.layout.activity_main)

        providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        btn_sign_out.setOnClickListener{
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener({
                    btn_sign_out.isEnabled=false
                    showSignInOptions()
                })
        }

        showSignInOptions()
    }

    fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            var response = IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                var user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    if(!user.isEmailVerified){
                        userExists(user)
                        verCorreo(user)
                    }
                    else{

                        finish()
                        return
                    }
                    //Toast.makeText(this,user.email,Toast.LENGTH_SHORT).show()
                }
                btn_sign_out.isEnabled = true
            }
        }
    }

    fun verCorreo(user: FirebaseUser){
        user.sendEmailVerification()
    }

    fun userExists(user:FirebaseUser){
        val docIdRef = db.collection("users").document(user.uid)
        docIdRef.get().addOnSuccessListener {document->
            if(!document.exists()){
                Toast.makeText(this,"se creo de nuevo",Toast.LENGTH_SHORT).show()
                val userh = hashMapOf(
                    "email" to user.email,
                    "name" to user.displayName
                )
                db.collection("users").document(user.uid).set(userh)
            }
            else {
                Toast.makeText(this,"Ya existe"+user.uid,Toast.LENGTH_SHORT).show()
                var kk = db.collection("partida").whereEqualTo("user",user.uid).get()
                kk.addOnSuccessListener { documents->
                    for(document in documents){
                        Log.d("TORTY","${document.data}")
                    }
                }
            }
            }
        docIdRef.get()
    }

}
