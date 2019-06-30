package com.bonobostudios.koklin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_patient_info.*
import java.text.SimpleDateFormat
import java.util.*

/*
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)

        val db = FirebaseFirestore.getInstance()

        BtnAceptar.setOnClickListener {
            var nombre = etNombrePaciente.text.toString()
            var edad = etNombrePaciente.text.toString()

            val pacient = hashMapOf(
                "Nombre" to nombre,
                "Edad" to edad
            )

            db.collection("pacientes").add(pacient)

        }

    }
}
 */
class PatientInfoActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)



       BtnAceptar.setOnClickListener{
           var opcion= rgLayout.checkedRadioButtonId

           var aux =""

            var nombre = etNombrePaciente.text.toString().trim()
            var edad = etEdadPaciente.text.toString()

            var user = FirebaseAuth.getInstance().currentUser?.uid

           var date = SimpleDateFormat("dd-MM-yyyy").format(Date()).toString()

           if (opcion==1){
               aux="M"
           }else if(opcion==2){
               aux="F"
           }



            val pacient = hashMapOf(
                "nombre" to nombre,
                "edad" to edad,
                "user" to user,
                "genero" to aux,
                "fecha" to date

            )

            db.collection("pacientes ").add(pacient)


            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
        BtnCancelar.setOnClickListener{
            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
