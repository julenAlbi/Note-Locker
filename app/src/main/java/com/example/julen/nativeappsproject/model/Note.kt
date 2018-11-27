package com.example.julen.nativeappsproject.model

import org.joda.time.DateTime
import java.io.Serializable

data class Note(val alias: String,
                val secret: String,
                val createDate: DateTime = DateTime.now(),
                val updateDate: DateTime = DateTime.now(),
                val locked: Boolean = false) : Serializable