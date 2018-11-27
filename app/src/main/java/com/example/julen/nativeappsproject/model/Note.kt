package com.example.julen.nativeappsproject.model

import java.util.*
import java.io.Serializable

data class Note(val alias: String,
                val secret: String,
                val createDate: Date,
                val updateDate: Date,
                val locked: Boolean) : Serializable