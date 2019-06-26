package com.bonobostudios.koklin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class TestActivity : AppCompatActivity()  {


    var namePac : String ="N/A"
    // var paciente = mIntent.getStringExtra("paciente")
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
        var elIntent = this.intent
        paciente = elIntent.getStringExtra("PACIENTE_ID")
        initMainFragment()
    }

    fun getSonido():String{
        var flag = 0
        while(flag ==0){
            sIndex = (1..3).random()
            if(!banned.contains(sIndex)){
                banned.add(sIndex)
                flag=1
            }
        }
        var sound = "sonido"+sIndex
        return sound
    }

    fun changeFragment(id: Int, frag: Fragment){
        supportFragmentManager.beginTransaction().replace(id, frag).commit() }


    fun initMainFragment(){
        mainFragment = ExerciseFragment.newInstance(getSonido())
        var resource = R.id.main_fragment
        changeFragment(resource,mainFragment)
    }

    fun insertEva(){
        val pacRef = db.collection("pacientes ").document(paciente)
        pacRef.get().addOnSuccessListener { document->
            if(document!=null){
                namePac = document.getString("nombre")!!
                val mEv = hashMapOf(
                    "paciente" to namePac,
                    "score" to nPreguntas,
                    "autor" to paciente
                )
                db.collection("evaluaciones ").add(mEv)
            }
        }
        pacRef.get()
    }

}
