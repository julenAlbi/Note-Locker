package com.example.julen.nativeappsproject.domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import java.io.Serializable

data class Note(val alias: String,
                val secret: String,
                val createDate: Date,
                val updateDate: Date) : Serializable