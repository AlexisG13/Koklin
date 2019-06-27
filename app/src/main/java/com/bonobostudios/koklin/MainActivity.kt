package com.bonobostudios.koklin

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonobostudios.Adapter.EvaluacionAdapter
import com.bonobostudios.Adapter.PacienteAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PacienteAdapter.OnPacienteSelectedListener,
    EvaluacionAdapter.OnEvaluacionSelectedListener {

    val rootRef = FirebaseFirestore.getInstance()
    lateinit var query: Query
    lateinit var query2: Query
    lateinit var query3: Query
    lateinit var adapter: PacienteAdapter
    lateinit var adapter2: EvaluacionAdapter
    private var referenciaPaciente = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        getSupportActionBar()?.setCustomView(R.layout.action_bar_layout)

        query = rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)


        ET_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                var busqueda= ET_search.text.toString().trim()
                if(busqueda=="") query3= rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)
                else query3=rootRef.collection("pacientes ").whereEqualTo("nombre",busqueda)
                adapter.setQuery(query3)
            }
        })



//ADAPTER
        adapter = object : PacienteAdapter(query, this@MainActivity) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    rvPacientes.visibility = View.GONE
                    //viewEmpty.visibility = View.VISIBLE

                } else {
                    rvPacientes.visibility = View.VISIBLE
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


        rvPacientes.layoutManager = LinearLayoutManager(this)
        rvPacientes.adapter = adapter


        actionProfile.setOnClickListener {
            val intent: Intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        actionSettings.setOnClickListener {
            val intent: Intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        StartANewTestIcon.setOnClickListener {
            val intent: Intent = Intent(this, PatientInfoActivity::class.java)
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
        referenciaPaciente = paciente.id
        query2 = rootRef.collection("evaluaciones ").whereEqualTo("autor", referenciaPaciente)
        adapter2 = object : EvaluacionAdapter(query2, this@MainActivity) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    rvResultados.visibility = View.GONE
                    Log.d("PENE", "NADA PAPS")
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
        rvResultados.layoutManager = LinearLayoutManager(this)
        rvResultados.adapter = adapter2
        adapter2.startListening()

        Toast.makeText(this, referenciaPaciente, Toast.LENGTH_SHORT).show()
        NuevoTestPacienteCreado.visibility = View.VISIBLE
        NuevoTestPacienteCreado.setOnClickListener {
            val pintent: Intent = Intent(this, TestActivity::class.java)
            pintent.putExtra("PACIENTE_ID",referenciaPaciente)
            startActivity(pintent)
        }

    }

    override fun onEvaluacionSelected(evaluacion: DocumentSnapshot) {
        Toast.makeText(this, "SIUUUUUUU", Toast.LENGTH_SHORT).show()

        val intent: Intent = Intent(this, EvDetailActivity::class.java)
        var pacienteID = intent.putExtra("EVALUACION_ID",evaluacion.id)
        startActivity(intent)

    }


}
