package com.bonobostudios.koklin

import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bonobostudios.koklin.Adapter.pacientesAdapter
import com.bonobostudios.koklin.POJOS.paciente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.pacientes_activity.*
import org.json.JSONObject

class pacientesActivity: AppCompatActivity(){

    private lateinit var  pacienteAdapter : pacientesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val db = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser



    private var pacienteList:ArrayList<paciente> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pacientes_activity)


        initRecyclerView()
        userExists(user!!)



    }


    fun addPacienteTList(aciente:paciente){
        pacienteList.add(aciente)
        pacienteAdapter.changeList(pacienteList)
    }

    fun userExists(user: FirebaseUser){
        val docIdRef = db.collection("users").document(user.uid)
        docIdRef.get().addOnSuccessListener {document->
            if(!document.exists()){
                Toast.makeText(this,"se creo de nuevo", Toast.LENGTH_SHORT).show()
                val userh = hashMapOf(
                    "email" to user.email,
                    "name" to user.displayName
                )
                db.collection("users").document(user.uid).set(userh)
            }
            else {
                Toast.makeText(this,"Ya existe"+user.uid, Toast.LENGTH_SHORT).show()
                var kk = db.collection("paciente ").whereEqualTo("user",user.uid).get()
                kk.addOnSuccessListener { documents->
                    for(document in documents){
                        Log.d("TORTY","${document.data}")

                        var pasty= document.toObject(paciente::class.java)
                        addPacienteTList(pasty)

                    }
                }
            }
        }
        docIdRef.get()
    }

    fun initRecyclerView(){
        viewManager = LinearLayoutManager(this)
        pacienteAdapter = pacientesAdapter(pacienteList,{pacienteIte: paciente-> pacienteIteClicked(pacienteIte)})

        rv_pacientes.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = pacienteAdapter
        }

    }

    private fun pacienteIteClicked (item: paciente){
        val pacienteBundle=Bundle()
        pacienteBundle.putParcelable("PACIENTE",item)
        //
    }







}