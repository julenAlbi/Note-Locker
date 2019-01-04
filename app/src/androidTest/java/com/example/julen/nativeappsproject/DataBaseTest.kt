package com.example.julen.nativeappsproject

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.storage.NoteDao
import com.example.julen.nativeappsproject.storage.NoteDataBase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DataBaseTest {
    private lateinit var noteDao: NoteDao

    @Before
    fun createDb() {
        noteDao = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), NoteDataBase::class.java).build().noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        NoteDataBase.destroyDataBase()
    }

    @Test
    fun insertNote(){
        val note = Note(0, "Testing", "This is a persistance testing.")
        noteDao.saveNote(note)
        val x = noteDao.getNoteByAlias("Testing")
        assertNotNull(x)
        assertEquals(note.toString(), x.toString())
    }

    @Test
    fun updateNote(){
        val noteToUpdate = Note(0, "to Update", "This is a update testing.")
        noteDao.saveNote(noteToUpdate)
        var x = noteDao.getNoteByAlias("to Update")
        x.alias = "note Updated"
        noteDao.updateNote(x)
        val x2 = noteDao.getNoteByAlias("note Updated")
        assertEquals(x.toString(), x2.toString())
    }

     @Test
     fun deleteNote(){
         val noteToDelete = Note(0, "to delete", "This is a delete testing.")
         noteDao.saveNote(noteToDelete)
         val toDelete = noteDao.getNoteByAlias("to delete")
         noteDao.deleteNote(toDelete)
         val x = noteDao.getNoteByAlias("to delete")
         assertNull(x)
     }

}