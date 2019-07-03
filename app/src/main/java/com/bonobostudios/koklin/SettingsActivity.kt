package com.bonobostudios.koklin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        //Función para dirigir al usuario a la pagina de Privacy Policy
        tv_pp.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://alexisg13.github.io/PrivacyPolicy"))
            startActivity(intent)
        }

        //Funcion para regresar a la pantalla principal 
        InformacionViewClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
