package com.example.julen.nativeappsproject.storage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.julen.nativeappsproject.model.Note

/**
 * The DAO of [NoteDataBase]
 */
@Dao
interface NoteDao {

    @Query("select * from Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * from note where alias = :alias")
    fun getNoteByAlias(alias: String): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}