package com.bonobostudios.POJOS

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class evaluacion(
   val FECHA :String ="N/A",
   val NRespuestas : String ="N/A",
   val autor : String = "N/A",
   val nivelAU : String = "N/A"
){
   companion object{
      const val FIELD_FECHA="fecha"
      const val FIELD_NRESPUESTA="NRespuesta"
      const val FIELD_AUTOR="autor"
      const val FIELD_NIVELAU="nivelAU"
   }
}
