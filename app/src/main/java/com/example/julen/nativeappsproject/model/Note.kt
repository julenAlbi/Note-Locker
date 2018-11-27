package com.example.julen.nativeappsproject.model

import java.util.*
import java.io.Serializable

data class Note(val alias: String,
                val secret: String,
                val createDate: Date = Date(),
                val updateDate: Date = Date(),
                val locked: Boolean = false) : Serializable