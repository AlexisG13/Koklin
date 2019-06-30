package com.bonobostudios.koklin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity()  {

    var nDoc =""
    var namePac : String ="N/A"
    val db = FirebaseFirestore.getInstance()
    var nPreguntas = 0
    var sIndex : Int = 0
    var banned = arrayListOf<Int>()
    var nActual = 0
    lateinit var mainFragment: ExerciseFragment
    var paciente = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        var elIntent = intent
        paciente = elIntent.getStringExtra("PACIENTE_ID")
        initMainFragment()
    }

    fun getSonido():String{
        var flag = 0
        while(flag ==0){
            sIndex = (1..30).random()
            if(!banned.contains(sIndex)){
                banned.add(sIndex)
                flag=1
            }
        }
        var sound = "sonido"+sIndex
        return sound
    }

    fun changeFragment(id: Int, frag: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            .replace(id, frag)
            .commit() }

    fun deleteFragment(frag: Fragment){
        supportFragmentManager.beginTransaction().remove(frag).commit()
    }

    fun initMainFragment(){
        mainFragment = ExerciseFragment.newInstance(getSonido())
        var resource = R.id.main_fragment
        changeFragment(resource,mainFragment)
    }

    fun insertEva(){
        var date = SimpleDateFormat("dd-MM-yyyy").format(Date()).toString()
        val pacRef = db.collection("pacientes ").document(paciente)
        pacRef.get().addOnSuccessListener { document->
            if(document!=null){
                namePac = document.getString("nombre")!!
                val mEv = hashMapOf(
                    "paciente" to namePac,
                    "score" to nPreguntas,
                    "autor" to paciente,
                    "fecha" to date
                )
                db.collection("evaluaciones ").add(mEv).addOnSuccessListener {document->
                    nDoc = document.id
                    var mIntent = Intent(this,EvDetailActivity::class.java)
                    mIntent.putExtra("EVALUACION_ID",nDoc)
                    startActivity(mIntent)
                    finish()
                }
            }
        }
        pacRef.get()
    }

}
