package com.bonobostudios.koklin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bonobostudios.koklin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.text.Layout
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.koklin.POJOS.paciente
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import kotlinx.android.synthetic.main.pacientes_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val rootRef = FirebaseFirestore.getInstance()

    val db = FirebaseFirestore.getInstance()
    private val REQUEST_CODE = 2019
    val auth = FirebaseAuth.getInstance()
    val query = rootRef.collection("pacientes ")
    val options = FirestoreRecyclerOptions.Builder<paciente>().setQuery(query,paciente::class.java).build()
    private var adapter= PacienteAdapterFirestore(options)

    lateinit var providers : List<AuthUI.IdpConfig>
    private var pacienteList:ArrayList<paciente> = ArrayList()
//    val options = FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(query, ProductModel::class.java).build()

private inner class pacienteViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view){
    internal fun setPacienteName(pacienteName:String){
        val textView = view.findViewById<TextView>(R.id.tv_nombrePaciente)
        textView.text=pacienteName
    }

}

    private inner class PacienteAdapterFirestore internal constructor(options: FirestoreRecyclerOptions<paciente>) : FirestoreRecyclerAdapter<paciente, pacienteViewHolder>(options){

        override fun onBindViewHolder(holder: pacienteViewHolder, position: Int, PA: paciente) {
holder.setPacienteName(PA.nombre)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pacienteViewHolder {
          val view=LayoutInflater.from(parent.context).inflate(R.layout.pacientes_cardview,parent,false)
            return pacienteViewHolder(view)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bonobostudios.koklin.R.layout.activity_main)

        rvPacientes.layoutManager=LinearLayoutManager(this)


        rvPacientes.adapter=adapter

        providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )


        showSignInOptions()



    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }
    fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build(),REQUEST_CODE)
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)





        if(requestCode == REQUEST_CODE){
            var response = IdpResponse.fromResultIntent(data)
            if(resultCode== Activity.RESULT_OK){
                var user = FirebaseAuth.getInstance().currentUser
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
                //btn_sign_out.isEnabled = true
            }
        }
    }

    fun verCorreo(user: FirebaseUser){
        user.sendEmailVerification()
    }

    fun usuario(user : FirebaseUser):String{
        return user.uid
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
                }
            }
            }
        docIdRef.get()
    }

}
