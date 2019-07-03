package com.bonobostudios.koklin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity()  {

    //Declaración de variables y valores
    var nDoc =""
    var namePac : String ="N/A"
    val db = FirebaseFirestore.getInstance()
    var nPreguntas = 0
    var sIndex : Int = 0
    //Array que contendra las sonidos que ya utilizamos
    var banned = arrayListOf<Int>()
    var nActual = 0
    lateinit var mainFragment: ExerciseFragment
    var paciente = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val elIntent = intent
        paciente = elIntent.getStringExtra("PACIENTE_ID")
        initMainFragment()
    }

    //Obtener un sonido al azar sin repetir
    fun getSonido():String{
        var flag = 0
        while(flag ==0){
            sIndex = (1..30).random()
            if(!banned.contains(sIndex)){
                banned.add(sIndex)
                flag=1
            }
        }
        val sound = "sonido"+sIndex
        return sound
    }

    //Funcion para cambiar de fragmento
    fun changeFragment(id: Int, frag: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            .replace(id, frag)
            .commit() }

    //Funcion para quitar el fragmento del stack y asi no retroceder a el
    fun deleteFragment(frag: Fragment){
        supportFragmentManager.beginTransaction().remove(frag).commit()
    }

    //Funcion para crear un nuevo fragmento y reemplazarlo con el anterior
    fun initMainFragment(){
        mainFragment = ExerciseFragment.newInstance(getSonido())
        var resource = R.id.main_fragment
        changeFragment(resource,mainFragment)
    }

    //Funcion para insertar una evaluación
    fun insertEva(){
        //Obtención de fecha y datos del paciente
        val date = SimpleDateFormat("dd-MM-yyyy").format(Date()).toString()
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
                //Inserción de la evaluación, en caso de exito llevar a la pantalla de detalle de evaluación
                db.collection("evaluaciones ").add(mEv).addOnSuccessListener {document->
                    nDoc = document.id
                    val mIntent = Intent(this,EvDetailActivity::class.java)
                    mIntent.putExtra("EVALUACION_ID",nDoc)
                    startActivity(mIntent)
                    finish()
                }
            }
        }
        pacRef.get()
    }

}
