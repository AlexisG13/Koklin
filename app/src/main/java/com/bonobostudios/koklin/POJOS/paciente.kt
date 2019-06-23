package com.bonobostudios.koklin.POJOS

import android.os.Parcel
import android.os.Parcelable

data class paciente(
    val Nombre:String ="N/A"


) : Parcelable{
    constructor(parcel: Parcel): this(
        Nombre=parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Nombre)
    }

    override fun describeContents() =0

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<paciente>{
            override fun createFromParcel(parcel: Parcel): paciente=paciente(parcel)

            override fun newArray(size: Int): Array<paciente?> = arrayOfNulls(size)
        }
    }



}