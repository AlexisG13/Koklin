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
import com.bonobostudios.POJOS.evaluacion

import com.bonobostudios.POJOS.paciente

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

    //private var evaAdapter : evaluacionAdapter?=null

    val db = FirebaseFirestore.getInstance()
    private val REQUEST_CODE = 2019
    val auth = FirebaseAuth.getInstance()

    lateinit var providers : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

//get pacientes
        query= rootRef.collection("pacientes ").whereEqualTo("user","EXyDrJaUolaKgFREAWehl82V9vu2")



        //Adapter

        adapter=object : PacienteAdapter(query,this@MainActivity){
            override fun onDataChanged() {
                if(itemCount==0){
                    rvPacientes.visibility= View.GONE
                    //viewEmpty.visibility = View.VISIBLE

                }else{
                    rvPacientes.visibility=View.VISIBLE
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

        showSignInOptions()
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
    fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.ic_banner)
            .build(),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            var response = IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                var user = FirebaseAuth.getInstance().currentUser
                val query = rootRef.collection("pacientes ").whereEqualTo("user",usuario(user!!))
                val options = FirestoreRecyclerOptions.Builder<paciente>().setQuery(query, paciente::class.java).build()




                if (user != null) {
                    if(!user.isEmailVerified){
                        userExists(user)
                        verCorreo(user)
                    }
                    else{

                        finish()
                        return
                    }
                    //Toast.makeText(this,user.email,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun usuario(user : FirebaseUser):String{
        return user.uid
    }








    fun verCorreo(user: FirebaseUser){
        user.sendEmailVerification()
    }

    fun userExists(user:FirebaseUser){
        val docIdRef = db.collection("users").document(user.uid)
        docIdRef.get().addOnSuccessListener {document->
            if(!document.exists()){
                Toast.makeText(this,"se creo de nuevo",Toast.LENGTH_SHORT).show()
                val userh = hashMapOf(
                    "email" to user.email,
                    "name" to user.displayName
                )
                db.collection("users").document(user.uid).set(userh)
            }
            else {




                Toast.makeText(this,"Ya existe"+user.uid,Toast.LENGTH_SHORT).show()
                var kk = db.collection("pacientes ").whereEqualTo("user",user.uid).get()


                kk.addOnSuccessListener { documents->
                    for(document in documents){
                        Log.d("TORTY","${document.data}")
                    }
                    NumeroDePacientes.setText(documents.size().toString())
                }
            }
            }
        docIdRef.get()
    }

}
