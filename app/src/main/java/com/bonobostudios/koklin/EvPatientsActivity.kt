package com.bonobostudios.koklin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonobostudios.Adapter.EvaluacionAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_ev_patients.*
import kotlinx.android.synthetic.main.activity_ev_patients.rvResultados



private var paciente=""
class EvPatientsActivity : AppCompatActivity(),EvaluacionAdapter.OnEvaluacionSelectedListener {

    private val rootRef = FirebaseFirestore.getInstance()
    lateinit var query: Query
    lateinit var adapter: EvaluacionAdapter







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ev_patients)

        val elIntent = intent
        paciente= elIntent.getStringExtra("LAREFERENCIA")

        query=rootRef.collection("evaluaciones ").whereEqualTo("autor", paciente)
        adapter = object : EvaluacionAdapter(query, this@EvPatientsActivity) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    rvResultados.visibility = View.GONE
                } else {
                    rvResultados.visibility = View.VISIBLE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Error: check logs for info.", Snackbar.LENGTH_LONG
                ).show()
            }
        }
        rvResultados.layoutManager=LinearLayoutManager(this)
        rvResultados.adapter=adapter

        botonmas.setOnClickListener {
            val pintent= Intent(this, TestActivity::class.java)
            pintent.putExtra("PACIENTE_ID", paciente)
            startActivity(pintent)
        }



        ResultadosViewClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()

        // Start listening for Firestore updates
        adapter.startListening()


    }

    public override fun onStop() {
        super.onStop()
        adapter.stopListening()
        //adapter2.stopListening()
    }



    override fun onEvaluacionSelected(evaluacion: DocumentSnapshot) {



    }

}
