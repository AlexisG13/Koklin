package com.bonobostudios.koklin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_patient_info.*

class PatientInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)

        BtnAceptar.setOnClickListener{
            val intent : Intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }
        BtnCancelar.setOnClickListener{
            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
