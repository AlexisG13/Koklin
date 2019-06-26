package com.bonobostudios.POJOS

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class paciente(
    val nombre:String ="N/A"


){
    companion object{
        const val FIELD_NOMBRE="nombre"
    }
}
