package com.example.julen.nativeappsproject.storage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.julen.nativeappsproject.model.Note

@Dao
interface NoteDao {

    @Query("select * from Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}