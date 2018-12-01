package com.example.julen.nativeappsproject.model

import org.joda.time.DateTime
import java.io.Serializable

data class Note(var alias: String,
                var secret: String,
                val createDate: DateTime = DateTime.now(),
                var updateDate: DateTime = DateTime.now(),
                var locked: Boolean = false) : Serializable