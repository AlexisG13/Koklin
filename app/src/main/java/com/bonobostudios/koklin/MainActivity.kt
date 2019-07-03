package com.bonobostudios.koklin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonobostudios.Adapter.PacienteAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PacienteAdapter.OnPacienteSelectedListener
     {

    val rootRef = FirebaseFirestore.getInstance()
    lateinit var query: Query
    lateinit var query3: Query
    lateinit var adapter: PacienteAdapter
    private var referenciaPaciente = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creamos la query que utlizaremos para buscar en la coleccion los pacientes que le pertenecen a el usuario logueado

        query = rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)

//El buscador que cada vez que se escriba texto ejecutara la busqueda
        ET_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            //La busqueda lo que hace es simplemente cambiar la query que esta usando el adapter para obtener la informacion
            override fun afterTextChanged(s: Editable) {
                val busqueda = ET_search.text.toString().trim()
                if (busqueda == "") query3 =
                    rootRef.collection("pacientes ").whereEqualTo("user", FirebaseAuth.getInstance().currentUser?.uid)
                else query3 = rootRef.collection("pacientes ").whereEqualTo("nombre", busqueda)
                adapter.setQuery(query3)
            }
        })



//Creamos un objeto adapter que sera de tipo PAcienteAdapter, de parametro le psamos la query
        //y el contexto de la actividad en que esta
        adapter = object : PacienteAdapter(query, this@MainActivity) {
            //Cuando cambia la data contamos la cantidad de pacientes

            override fun onDataChanged() {
                if (itemCount == 0) {
                    rvPacientes.visibility = View.GONE

//Si la cantidad de pacientes es mayor que 0 se muestra el recycler view y agregamos esa cantidad a nuestro contador de pacientes

                } else {
                    rvPacientes.visibility = View.VISIBLE
                    NumeroDePacientes.text=itemCount.toString()
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
            val intent = Intent(this, PatientInfoActivity::class.java)
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
                        val intent= Intent(this, SettingsActivity::class.java)
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

    }


    public override fun onStart() {
        super.onStart()
        // Escucha el adapter los cambios en la BD
        adapter.startListening()


    }

    public override fun onStop() {
        super.onStop()
        adapter.stopListening()
        //Deja de escuchar ambios en la BD
    }

    override fun onPacienteSelected(paciente: DocumentSnapshot) {
        referenciaPaciente = paciente.id
        //Cuando seleccionamos un paciente enviamos la referencia de su id a otra actividad

val capi = Intent(this,EvPatientsActivity::class.java)
        capi.putExtra("LAREFERENCIA",referenciaPaciente)
        startActivity(capi)



    }




}
