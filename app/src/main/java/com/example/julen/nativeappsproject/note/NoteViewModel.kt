package com.example.julen.nativeappsproject.note

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import com.example.julen.nativeappsproject.base.InjectedViewModel
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.model.NoteTranslate
import com.example.julen.nativeappsproject.storage.NoteRepository
import com.example.julen.nativeappsproject.util.*
import com.example.julen.translations.network.TranslateAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NoteViewModel(noteSelected : Note?, val application: Application) : InjectedViewModel() {

    @Inject
    lateinit var translateAPI: TranslateAPI

    var note = MutableLiveData <Note>()
    var enes = MutableLiveData <Boolean>()
    var esen = MutableLiveData <Boolean>()
    var progressBarVisivility = MutableLiveData <Int>()

    /**
     * Represents a disposable resources
     */
    private lateinit var subscription: Disposable

    internal val toastMessage = SingleLiveEvent<String>()

    private var noteRepo: NoteRepository

    init {
        if(noteSelected != null){
            this.note.value = noteSelected
        }
        noteRepo = NoteRepository(application)
        esen.value = true
        enes.value = false
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

    fun onTranslateButtonClicked(button: View){
        val direction : String
        if(esen.value!!){
            direction = DIRECT_ESEN
        }else{
            direction = DIRECT_ENES
        }
        subscription = translateAPI.translateRequest(KEY_API, note.value!!.secret, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onTranslateStart() }
                .doOnTerminate{ onTranslateFinished() }
                .subscribe(
                        {result -> onTranslateSuccess(result)},
                        {error -> onTranslateError(error)}
                )
    }

    private fun onTranslateError(error: Throwable) {
        Log.e(TRANSLATE_ERROR , error.message!!)
    }

    private fun onTranslateSuccess(result: NoteTranslate){

        when(result.code){
            200 -> {
                note.value!!.secret = result.text.get(0)
                note.postValue(note.value!!)
            }
            401 -> {
                Log.d(TRANSLATE_ERROR, "Invalid API key")
                toastMessage.value = "Invalid API key"
            }
            402 -> {
                Log.d(TRANSLATE_ERROR, "Blocked API key")
                toastMessage.value = "Blocked API key"
            }
            404 -> {
                Log.d(TRANSLATE_ERROR, "Exceeded the daily limit on the amount of translated text")
                toastMessage.value = "Exceeded the daily limit on the amount of translated text"
            }
            413 -> {
                Log.d(TRANSLATE_ERROR, "Exceeded the maximum text size")
                toastMessage.value = "Exceeded the maximum text size"
            }
            422 -> {
                Log.d(TRANSLATE_ERROR, "The text cannot be translated")
                toastMessage.value = "The text cannot be translated"
            }
            501 -> {
                Log.d(TRANSLATE_ERROR, "The specified translation direction is not supported")
                toastMessage.value = "The specified translation direction is not supported"
            }
        }
    }

    private fun onTranslateFinished() {
        progressBarVisivility.value = View.INVISIBLE
    }

    private fun onTranslateStart() {
        progressBarVisivility.value = View.VISIBLE
    }

    /**
     * Disposes the subscription when the [InjectedViewModel] is no longer used.
     */
    override fun onCleared() {
        super.onCleared()
        if (::subscription.isInitialized){
            subscription.dispose()
        }
    }
}

class NoteViewModelFactory(private val note : Note?, private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(note, application) as T
    }
}