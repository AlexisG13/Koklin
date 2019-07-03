package com.bonobostudios.koklin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    //Declaración de variables : BD , autenticación y REQUEST_CODE
    val db = FirebaseFirestore.getInstance()
    private val REQUEST_CODE = 2019
    val auth = FirebaseAuth.getInstance()
    lateinit var providers : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Añadiendo los proveedores para autenticación : Email y Google
        providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        //Verificar si ya hay un usuario logeado
        if(auth.currentUser==null){
            showSignInOptions()
        }
        else {var mIntent = Intent(this,MainActivity::class.java)
        startActivity(mIntent)}

    }
    
    //Mostrar las opciones para logearse 
    fun showSignInOptions(){
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.koklin_banner)
                .build(),REQUEST_CODE)
    }

    //Funcion para verificar log-in del usuario y obtener su instancia
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                var user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                        userExists(user)
                }
            }
        }
    }

    //Funcion para verificar si es primera vez que entra el usuario, si es asi crear su documento en la BD
    fun userExists(user: FirebaseUser){
        var mIntent = Intent(this,MainActivity::class.java)
        val docIdRef = db.collection("users").document(user.uid)
        docIdRef.get().addOnSuccessListener {document->
            if(!document.exists()){
                val userh = hashMapOf(
                    "email" to user.email,
                    "name" to user.displayName
                )
                db.collection("users").document(user.uid).set(userh)

            }
            else {
            }
        }
        docIdRef.get()
        mIntent.putExtra("USER_ID",user.uid)
        startActivity(mIntent)
        finish()
    }
}
