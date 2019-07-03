package com.bonobostudios.POJOS

import com.google.firebase.firestore.IgnoreExtraProperties

//Clase que servira, para decir que campos quiero obtener de una coleccion de firestore
//en caso uno de esos campos este vacio se mostrara el valor por defecto

@IgnoreExtraProperties
data class paciente(
    val nombre:String ="N/A",
    val edad:String="na",
    val genero:String="Na",
    val fecha:String="na"



){
    companion object{
        const val FIELD_NOMBRE="nombre"
        const val FIELD_EDAD="edad"
        const val FIELD_GENERO="genero"
        const val FIELD_FECHA="fecha"

    }
}
