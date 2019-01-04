package com.example.julen.nativeappsproject.storage

import android.app.Application
import android.arch.lifecycle.LiveData
import com.example.julen.nativeappsproject.model.Note
import org.jetbrains.anko.doAsync

/**
 * A repository which can be used to access data. It provides an abstraction layout between the different data sources and the rest of the app.
 * @param application Application object that it is needed to instantiate the database.
 * @constructor gets the Dao of the database and instantiates LiveData of all notes.
 */
class NoteRepository (application: Application) {

    private lateinit var dao: NoteDao
    private lateinit var allNotes: LiveData<List<Note>>

    init {
        dao = NoteDataBase.getNoteDataBase(application)!!.noteDao()
        allNotes = dao.getAllNotes()
    }

    /**
     * @return the LiveData list object of [Note].
     */
    fun getAllNotes() : LiveData<List<Note>>{
        return allNotes
    }

    /**
     * Inserts a [Note] in the database.
     */
    fun insertNote(note:Note){
        doAsync {
            dao.saveNote(note)
        }
    }

    /**
     * Updates a [Note] in the database.
     */
    fun updateNote(note:Note){
        doAsync {
            dao.updateNote(note)
        }
    }

    /**
     * Deletes a [Note] from the database.
     */
    fun deleteNote(note: Note){
        doAsync {
            dao.deleteNote(note)
        }
    }

}