package com.example.julen.nativeappsproject.note

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.model.NoteTranslate
import com.example.julen.nativeappsproject.storage.NoteRepository
import com.example.julen.nativeappsproject.util.*
import com.example.julen.translations.network.TranslateAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * It is a viewmodel.
 *
 * @property note
 * @property progressBarVisivility boolean: if the progress bar is visible. It is visible when the note is translating
 * @property subscription represents a disposable resources
 * @property toastMessage it is used to send toast message
 * @property noteRepo repository to communicate with the database
 */
class NoteViewModel(noteSelected : Note?, val application: Application): ViewModel() {

    var note = MutableLiveData <Note>()
    var progressBarVisivility = MutableLiveData <Int>()

    private lateinit var subscription: Disposable

    internal val toastMessage = SingleLiveEvent<String>()

    private var noteRepo: NoteRepository

    init {
        if(noteSelected != null){
            this.note.value = noteSelected
        }
        noteRepo = NoteRepository(application)
        progressBarVisivility.value = View.INVISIBLE
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