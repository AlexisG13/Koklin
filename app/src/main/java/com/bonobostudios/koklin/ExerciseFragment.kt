package com.bonobostudios.koklin

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.exercise_fragment.*

class ExerciseFragment : Fragment(){

    var mp: MediaPlayer? = null
    var intentos = 3
    var nPreguntas = 0
    lateinit var resp1 : TextView
    lateinit var resp2 : TextView
    lateinit var resp3 : TextView
    lateinit var ptText : TextView
    lateinit var xxx: TestActivity
    private lateinit var btn : Button
    val db = FirebaseFirestore.getInstance()
    lateinit var sonido : String

    companion object{
        fun newInstance(sonido : String) : ExerciseFragment{
                val newFragment = ExerciseFragment()
                newFragment.sonido = sonido
                return newFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.exercise_fragment,container,false)
        bind(view)
        xxx = activity as TestActivity
        ReadAllQuestions()
        return view
    }

    private fun bind(view:View){
        resp1 = view.findViewById(R.id.respuesta1)
        resp2 = view.findViewById(R.id.respuesta2)
        resp3 = view.findViewById(R.id.respuesta3)
        ptText = view.findViewById(R.id.PreguntaID)
        btn = view.findViewById(R.id.btn_play)
        var son = resources.getIdentifier(sonido,"raw", context?.packageName)
        var mp = MediaPlayer.create(context,son)
        mp?.setOnCompletionListener {
            btn.isEnabled=true
            if(intentos==0) btn.isEnabled=false
        }
        btn.setOnClickListener{
            if(intentos>=1){
                btn.isEnabled=false
                mp.start()
                intentos-=1
                var n = IntentosRestantes.text.toString().toInt()
                n -= 1
                IntentosRestantes.text = n.toString()
            }
        }
    }

    private fun ReadAllQuestions() {
        db.collection("nivel")
            .get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    if (task.isSuccessful) {
                        val pReference = db.collection("sounds").document(sonido)
                        ReadAllAnswers(pReference, 1)
                    } else {
                        Log.w("WARNING!", "No se pudo encontrar nada que cargar.", task.exception)
                    }
                }
            }
    }

    private fun ReadAllAnswers(reference: DocumentReference, pActual: Int) {
        reference.get().addOnSuccessListener { document ->
            val pregunta = document.get("pregunta").toString()
            for (actual in 1..3) {
                val respuestasReference =
                    reference.collection("options").document("opt$actual")
                respuestasReference.get().addOnSuccessListener { document ->
                    val respuesta = document.get("text").toString()
                    val esCorrecta = document.get("esCorrecta").toString()
                    if (document != null) {
                        ptText.setText(pregunta)
                        if ("opt$actual" == "opt1") {
                            loadThings(resp1,respuesta,esCorrecta,pActual)
                        } else if ("opt$actual" == "opt2") {
                            loadThings(resp2,respuesta,esCorrecta,pActual)
                        } else if ("opt$actual" == "opt3") {
                            loadThings(resp3,respuesta,esCorrecta,pActual)
                        }
                    } else {
                        Log.w("ERROR!", "Algo pasó porque no estamos en vivo.")
                    }
                }
            }
        }
    }

    private fun loadThings(context : TextView, respuesta:String, esCorrecta:String, pActual: Int){
        context.setText(respuesta)
        context.setOnClickListener {
            //var intentosDeX = intentos
            if (esCorrecta == "true") {
                xxx.nPreguntas+=1
                context.setBackgroundColor(Color.argb(255, 41, 181, 48))
            } else {
                context.setBackgroundColor(Color.argb(255, 181, 41, 48))
            }
            Handler().postDelayed({
                xxx.nActual+=1
                if(xxx.nActual==3){
                    xxx.insertEva()
                    var mIntent = Intent(activity,MainActivity::class.java)
                    startActivity(mIntent)
                }
                else  xxx.initMainFragment()
            },200)
        }
    }
}