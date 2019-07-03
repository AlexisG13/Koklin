package com.bonobostudios.POJOS

import com.google.firebase.firestore.IgnoreExtraProperties

//Clase que servira, para decir que campos quiero obtener de una coleccion de firestore
//en caso uno de esos campos este vacio se mostrara el valor por defecto
@IgnoreExtraProperties
data class evaluacion(
   val fecha :String ="N/A",
   val score : Int =0,
   val autor : String = "N/A",
   val paciente : String = "N/A"
){
   companion object{
      const val FIELD_FECHA="fecha"
      const val FIELD_NRESPUESTA="NRespuesta"
      const val FIELD_AUTOR="autor"
      const val FIELD_NIVELAU="nivelAU"
   }
}
