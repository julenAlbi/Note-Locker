package com.example.julen.nativeappsproject.note

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.storage.NoteRepository

class NoteViewModel(noteSelected : Note?, application: Application) : AndroidViewModel(application) {

    var note = MutableLiveData <Note>()
    private lateinit var noteRepo: NoteRepository

    init {

        if(noteSelected != null){
            this.note.value = noteSelected
        }
        noteRepo = NoteRepository(application)
    }

    fun update(){
        noteRepo.updateNote(note.value!!)
    }

    fun insert(){
        noteRepo.insertNote(note.value!!)
    }

    fun delete(){
        noteRepo.deleteNote(note.value!!)
    }
}

class NoteViewModelFactory(private val note : Note?, private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(note, application) as T
    }
}