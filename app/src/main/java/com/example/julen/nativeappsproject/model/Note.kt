package com.example.julen.nativeappsproject.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import org.joda.time.DateTime
import java.io.Serializable
import com.example.julen.nativeappsproject.Converter

@Entity
data class Note(
        @ColumnInfo(name="id")
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name="alias")
        var alias: String,
        @ColumnInfo(name="secret")
        var secret: String,
        @TypeConverters(Converter::class)
        @ColumnInfo(name="create_date")
        val createDate: DateTime = DateTime.now(),
        @TypeConverters(Converter::class)
        @ColumnInfo(name="update_date")
        var updateDate: DateTime = DateTime.now(),
        @ColumnInfo(name="locked")
        var locked: Boolean = false) : Serializable