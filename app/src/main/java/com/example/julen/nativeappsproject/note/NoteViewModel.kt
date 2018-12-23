package com.example.julen.nativeappsproject.note

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.example.julen.nativeappsproject.NoteDataBase
import com.example.julen.nativeappsproject.model.Note

class NoteViewModel(noteSelected : Note, application: Application) : AndroidViewModel(application) {

    var note = MutableLiveData <Note>()

    init {
        this.note.value = noteSelected
        Log.d("NOTE VALUEEE:", note.value.toString())
    }
}

class NoteViewModelFactory(private val note : Note, private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(note, application) as T
    }
}