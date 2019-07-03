package com.bonobostudios.Adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*


//Clase abstracta para poder implementar sus metodos en las otras clases adapter que tenemos

abstract class FirestoreAdapter <VH : RecyclerView.ViewHolder>(private var query: Query?) :
    RecyclerView.Adapter<VH>(),
    EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null

    private val snapshots = ArrayList<DocumentSnapshot>()

    override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            Log.w(TAG, "onEvent:error", e)
            onError(e)
            return
        }

        if (documentSnapshots == null) {
            return
        }

        // Envia el evento
        Log.d(TAG, "onEvent:numChanges:" + documentSnapshots.documentChanges.size)
        for (change in documentSnapshots.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

        onDataChanged()
    }
//Funcion que nos servira para "escuchar" cambios en la BD de firestore
    fun startListening() {
        if (query != null && registration == null) {
            registration = query!!.addSnapshotListener(this)
        }
    }
    ////Funcion que nos servira para dejar de "escuchar2 cambios en la BD de firestore
    fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        notifyDataSetChanged()
    }

    //Esta funcion la usamos por si queremos cambiar la query que esta ejecutando el adapter

    fun setQuery(query: Query) {
        // Primero deja de escuchar
        stopListening()

        // Borra la data ya existente
        snapshots.clear()
        notifyDataSetChanged()

        //Empieza a escuchar la nueva query
        this.query = query
        startListening()
    }

    open fun onError(e: FirebaseFirestoreException) {
        Log.w(TAG, "onError", e)
    }

    open fun onDataChanged() {}

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshots[index]
    }


    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }



    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    companion object {

        private const val TAG = "FirestoreAdapter"
    }


 }