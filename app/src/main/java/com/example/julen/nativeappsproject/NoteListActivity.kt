package com.example.julen.nativeappsproject

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import com.example.julen.nativeappsproject.authentication.AuthenticationDialog
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.extensions.startNoteActivity
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import org.joda.time.DateTime
import java.util.*

class NoteListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private var notes: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        addSecretButton.setOnClickListener { view ->
            TODO("Add new note.")
        }

        if (noteFrame != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        notes = getFakeNotes()

        toolbar.title = title
        note_list.adapter = NoteListAdapter(this, notes!!, twoPane)
        note_list.layoutManager = LinearLayoutManager(this)
        (note_list.adapter as NoteListAdapter).showNoteActivity = { note ->
            if(note.locked){
                val authDialog = AuthenticationDialog()
                authDialog.passwordVerificationListener = {validatePassword(it)}
                authDialog.authenticationSuccessListener = {startNoteActivity(note)}
                authDialog.show(supportFragmentManager, "Authenticate")
            }else{
                startNoteActivity(note)
            }
        }
        (note_list.adapter as NoteListAdapter).showNoteFragment = { note ->

            if(note.locked){
                val authDialog = AuthenticationDialog()
                authDialog.passwordVerificationListener = {validatePassword(it)}
                authDialog.authenticationSuccessListener = {
                    val fragment = NoteFragment.newInstance(note)
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.noteFrame, fragment)
                            .commit()
                }
                authDialog.show(supportFragmentManager, "Authenticate")
            }else {
                val fragment = NoteFragment.newInstance(note)
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.noteFrame, fragment)
                        .commit()
            }
        }
    }

    /**
     * Validate password inputted from Authentication Dialog.
     */
    private fun validatePassword(inputPassword: String): Boolean {
        val sharedPreferencesManager= SharedPreferencesManager(this)
        return EncryptionServices(applicationContext).decrypt(sharedPreferencesManager.getPassword()) == inputPassword
    }

    private fun getFakeNotes(): List<Note>? {
        val noteList = mutableListOf<Note>()
        noteList.add(Note("pasahitzak", "iupsiub:fudayby", DateTime.now(), DateTime.now(), true))
        noteList.add(Note("sekretuek", "ouygouydvsoytvsodvtsdp", DateTime.now(), DateTime.now(), true))
        noteList.add(Note("beste sekretue", "byfofue fdouyav wpuey", DateTime.now(), DateTime.now(), false))
        return noteList
    }

}
