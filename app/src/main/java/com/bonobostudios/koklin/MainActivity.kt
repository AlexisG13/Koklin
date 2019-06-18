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
import com.google.firebase.firestore.core.UserData
import java.util.*


class MainActivity : AppCompatActivity() {

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
                        verCorreo(user)
                    }
                    else{

                        finish()
                        return
                    }
                    Toast.makeText(this,user.email,Toast.LENGTH_SHORT).show()
                }
                btn_sign_out.isEnabled = true
            }
        }
    }

    fun verCorreo(user: FirebaseUser){
        user.sendEmailVerification()
    }

}
