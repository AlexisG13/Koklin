package com.bonobostudios.koklin

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.PopupMenu
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
import kotlinx.android.synthetic.main.pacientes_cardview.*


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

        query = rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)


        ET_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                var busqueda = ET_search.text.toString().trim()
                if (busqueda == "") query3 =
                    rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)
                else query3 = rootRef.collection("pacientes ").whereEqualTo("nombre", busqueda)
                adapter.setQuery(query3)
            }
        })




        adapter = object : PacienteAdapter(query, this@MainActivity) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    rvPacientes.visibility = View.GONE


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

        StartANewTestIcon.setOnClickListener {
            val intent: Intent = Intent(this, PatientInfoActivity::class.java)
            startActivity(intent)
        }

        NavigationDrawer.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.opt_perfil -> {
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.opt_informacion -> {
                        val intent: Intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.navigation_drawer)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("ERROR ICON:", "No se muestran los íconos de  los menú", e)
            } finally {
                popupMenu.show()
            }
        }

        MenuEB.setOnClickListener{
            val popupMenu = PopupMenu(this, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.opt_edit -> {
                        Toast.makeText(this,"Click en edit",Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.opt_delete -> {
                        Toast.makeText(this,"Click en delete",Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.popup_paciente_menu)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("ERROR ICON:", "No se muestran los íconos de  los menú", e)
            } finally {
                popupMenu.show()
            }
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
        referenciaPaciente = paciente.id
        query2 = rootRef.collection("evaluaciones ").whereEqualTo("autor", referenciaPaciente)
        adapter2 = object : EvaluacionAdapter(query2, this@MainActivity) {
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
        rvResultados.layoutManager = LinearLayoutManager(this)
        rvResultados.adapter = adapter2
        adapter2.startListening()


        NuevoTestPacienteCreado.visibility = View.VISIBLE
        NuevoTestPacienteCreado.setOnClickListener {
            val pintent: Intent = Intent(this, TestActivity::class.java)
            pintent.putExtra("PACIENTE_ID", referenciaPaciente)
            startActivity(pintent)
        }

    }

    override fun onEvaluacionSelected(evaluacion: DocumentSnapshot) {


        val intent: Intent = Intent(this, EvDetailActivity::class.java)
        var pacienteID = intent.putExtra("EVALUACION_ID", evaluacion.id)
        startActivity(intent)

    }


}
