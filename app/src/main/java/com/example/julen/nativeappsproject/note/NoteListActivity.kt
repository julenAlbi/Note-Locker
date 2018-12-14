package com.example.julen.nativeappsproject.note

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.authentication.AuthenticationDialog
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.extensions.startNoteActivity
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*
import org.joda.time.DateTime

class NoteListActivity : AppCompatActivity(), FragmentCommunication {
    override fun changeFragment(note: Note?) {
        if(twoPaneMode.equals(NoteActivity.ADD_EDIT)){
            twoPaneMode = NoteActivity.VIEW_NOTE

        }else{
            twoPaneMode = NoteActivity.ADD_EDIT
        }
        if(twoPaneMode.equals(NoteActivity.ADD_EDIT)){
            val fragment = AddNoteFragment.newInstance(note)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.noteFrame, fragment)
                    .commit()
        }else{
            val fragment = NoteFragment.newInstance(note!!)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.noteFrame, fragment)
                    .commit()
        }
        invalidateOptionsMenu()
    }

    private var twoPane: Boolean = false

    private var notes: List<Note>? = null


    companion object {
        private var twoPaneMode: String? =  null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        addSecretButton.setOnClickListener {
            if(twoPane){
                twoPaneMode = NoteActivity.ADD_EDIT
                val fragment = AddNoteFragment.newInstance()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.noteFrame, fragment)
                        .commit()
            }else{
                startNoteActivity(mode = NoteActivity.ADD_EDIT)
            }
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
                authDialog.authenticationSuccessListener = {startNoteActivity(note, NoteActivity.VIEW_NOTE)}
                authDialog.show(supportFragmentManager, "Authenticate")
            }else{
                startNoteActivity(note, NoteActivity.VIEW_NOTE)
            }
        }
        (note_list.adapter as NoteListAdapter).showNoteFragment = { note ->

            twoPaneMode = NoteActivity.VIEW_NOTE
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        twoPaneMode?.let {
            menuInflater.inflate(R.menu.note_menu,menu)
            if(it.equals(NoteActivity.ADD_EDIT)){
                menu?.findItem(R.id.action_save)?.isVisible = true
                menu?.findItem(R.id.action_edit)?.isVisible = false
            }else{
                menu?.findItem(R.id.action_edit)?.isVisible = true
                menu?.findItem(R.id.action_save)?.isVisible = false
            }
            return true
        }
        return false //return false if nothing is done
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
