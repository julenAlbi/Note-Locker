package com.example.julen.nativeappsproject.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import org.joda.time.DateTime
import java.io.Serializable
import com.example.julen.nativeappsproject.Converter

/**
 * Note data class representing a note.
 *
 * @property id The id of the note
 * @property alias The alias or the title of the note
 * @property secret The content of the note
 * @property createDate The date of the creation of the note
 * @property updateDate The date of the last update of the note
 * @property locked A boolean property that indicates whether the note must be encrypted or not. if it is locked,
 * it is necessary to write the password before open the note, otherwise not
 */
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
        var locked: Boolean = false) : Serializable{
        override fun toString(): String = "Alias: " + alias + "\nNote: " + secret + "\nCretion Date: " + createDate.toString("dd/MM/yyyy") +
                "\nUpdate date: " + updateDate.toString("dd/MM/yyyy") + "\nLocked: " + locked.toString()
}