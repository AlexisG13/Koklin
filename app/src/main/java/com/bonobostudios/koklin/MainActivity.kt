package com.bonobostudios.koklin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonobostudios.Adapter.EvaluacionAdapter
import com.bonobostudios.Adapter.PacienteAdapter

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(),PacienteAdapter.OnPacienteSelectedListener,
    EvaluacionAdapter.OnEvaluacionSelectedListener {

    val rootRef = FirebaseFirestore.getInstance()
    lateinit var query: Query
    lateinit var query2: Query
    lateinit var adapter: PacienteAdapter
    lateinit var adapter2 : EvaluacionAdapter
    private var referenciaPaciente = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        query= rootRef.collection("pacientes ").whereEqualTo("user","EXyDrJaUolaKgFREAWehl82V9vu2")

//ADAPTER
        adapter=object : PacienteAdapter(query,this@MainActivity){
            override fun onDataChanged() {
                if(itemCount==0){
                    rvPacientes.visibility= View.GONE
                    //viewEmpty.visibility = View.VISIBLE

                }else{
                    rvPacientes.visibility= View.VISIBLE
                }

            }
            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }


        rvPacientes.layoutManager=LinearLayoutManager(this)
        rvPacientes.adapter=adapter


        actionProfile.setOnClickListener {
            val intent : Intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        actionSettings.setOnClickListener {
            val intent : Intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }

        StartANewTestIcon.setOnClickListener {
            val intent : Intent = Intent(this,PatientInfoActivity::class.java)
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

    override fun onPacienteSelected(paciente: DocumentSnapshot) {
        //Toast.makeText(this,"SIUUUUUUU",Toast.LENGTH_SHORT).show()
        referenciaPaciente=paciente.id
        query2=rootRef.collection("evaluaciones ").whereEqualTo("autor",referenciaPaciente)
        adapter2=object : EvaluacionAdapter(query2,this@MainActivity){
            override fun onDataChanged() {
                if (itemCount==0){
                    rvResultados.visibility=View.GONE
                    Log.d("PENE","NADA PAPS")
                }else{
                    rvResultados.visibility=View.VISIBLE
                }
            }  override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }
        rvResultados.layoutManager=LinearLayoutManager(this)
        rvResultados.adapter=adapter2
        adapter2.startListening()

        Toast.makeText(this,referenciaPaciente,Toast.LENGTH_SHORT).show()




    }

    override fun onEvaluacionSelected(evaluacion: DocumentSnapshot){
        Toast.makeText(this,"SIUUUUUUU",Toast.LENGTH_SHORT).show()

        val intent : Intent = Intent(this,TestActivity::class.java)
        startActivity(intent)

    }




}
