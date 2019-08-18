package com.example.julen.nativeappsproject.note

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.authentication.AuthenticationDialog
import com.example.julen.nativeappsproject.encription.EncryptionServices
import com.example.julen.nativeappsproject.extensions.startChangePasswordActivity
import com.example.julen.nativeappsproject.extensions.startNoteActivity
import com.example.julen.nativeappsproject.model.Note
import com.example.julen.nativeappsproject.storage.NoteRepository
import com.example.julen.nativeappsproject.storage.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*

/**
 * This activity shows the list of the notes (recyclerview) and a floating action button to add a new note.
 *
 * @property noteRepo repository to communicate with the database.
 * @property twoPane indicates if the activity is on two pane mode. If the density of the screen is higher than 900dp,
 * [twoPane] will be true, and the notes are going to display and edit in the same activity, at the right side of the screen.
 * That means that the [AddNoteFragment] and [NoteFragment] will be displayed in this activity.
 * @property twoPaneMode represents the mode of the activity if it is on two pane mode. If the user wants to edit or add a note
 * ([AddNoteFragment]), the value of [twoPaneMode] will be [NoteActivity.EDIT_NOTE] or [NoteActivity.ADD_NOTE]. If the user wants
 * to see a note ([NoteFragment]), the value of [twoPaneMode] will be [NoteActivity.VIEW_NOTE].
 */
class NoteListActivity : AppCompatActivity(), FragmentCommunication {

    /**
     * It overrides the interface [FragmentCommunication] in order to change the fragments.
     */
    override fun changeFragment() {
        twoPaneMode = if(twoPaneMode.equals(NoteActivity.ADD_NOTE) || twoPaneMode.equals(NoteActivity.EDIT_NOTE)){
            NoteActivity.VIEW_NOTE

        }else{
            NoteActivity.EDIT_NOTE
        }
        if(twoPaneMode.equals(NoteActivity.ADD_NOTE) || twoPaneMode.equals(NoteActivity.EDIT_NOTE)){
            val fragment = AddNoteFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.noteFrame, fragment)
                    .commit()
        }else{
            val fragment = NoteFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.noteFrame, fragment)
                    .commit()
        }
        invalidateOptionsMenu()
    }

    private lateinit var noteRepo: NoteRepository

    companion object {
        var twoPane: Boolean = false
    }
    private var twoPaneMode: String? =  null

    /**
     * Performs an action when menu [item] is selected. In [AddNoteFragment] case, the user maybe wants
     * to save the note or to delete the note.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_add_note -> {
                if (twoPane) {
                    twoPaneMode = NoteActivity.ADD_NOTE
                    val fragment = AddNoteFragment.newInstance(Note(0, "", ""))
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.noteFrame, fragment)
                            .commit()
                } else {
                    startNoteActivity(mode = NoteActivity.ADD_NOTE)
                }
                return true
            }
            R.id.action_change_password -> {
                startChangePasswordActivity()
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)
//        this.title = "Notes" //Cannot set title to toolbar

        if (noteFrame != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        noteRepo = NoteRepository(application)

        //Get all notes LiveData notes from reposiroty
        val noteListObserver = Observer<List<Note>> {
            (note_list.adapter as NoteListAdapter).setNotes(it!!)
        }
        noteRepo.getAllNotes().observe(this, noteListObserver)

        toolbar.title = title
        note_list.adapter = NoteListAdapter(twoPane)
        note_list.layoutManager = LinearLayoutManager(this)
        /**
         * Lambda implementaions for the recyclerview's adapter.
         *
         * If the activity is running on twoPane mode, a fragment will be displayed ([NoteListAdapter.showNoteFragment]),
         * else, new activitiy will be created ([NoteListAdapter.showNoteActivity])
         */
        (note_list.adapter as NoteListAdapter).showNoteActivity = { note ->
            if(note.locked){
                //If the note is locked, needs password to decrypt the note
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
                //If the note is locked, needs password to decrypt the note
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
     * Makes visible menu items.
     *
     * If the activity is on twoPane mode, the menu menu items will be visible, depending on if the user
     * is seeing a note or editing a note.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        twoPaneMode?.let {
            if(it == NoteActivity.ADD_NOTE || it == NoteActivity.EDIT_NOTE){
                menu?.findItem(R.id.action_save)?.isVisible = true
                menu?.findItem(R.id.action_edit)?.isVisible = false
                menu?.findItem(R.id.action_add_note)?.isVisible = false
            }else{
                menu?.findItem(R.id.action_edit)?.isVisible = true
                menu?.findItem(R.id.action_save)?.isVisible = false
                menu?.findItem(R.id.action_add_note)?.isVisible = true
            }
            menu?.findItem(R.id.action_change_password)?.isVisible = true
            return true
        } ?: run{
            menu?.findItem(R.id.action_add_note)?.isVisible = true
            menu?.findItem(R.id.action_change_password)?.isVisible = true
            menu?.findItem(R.id.action_edit)?.isVisible = false
            menu?.findItem(R.id.action_save)?.isVisible = false
            menu?.findItem(R.id.action_delete)?.isVisible = false
            return true
        }
        return false //returns false if nothing is done
    }

    /**
     * Validate password inputted from Authentication Dialog.
     */
    private fun validatePassword(inputPassword: String): Boolean {
        val sharedPreferencesManager= SharedPreferencesManager(this)
        return EncryptionServices(applicationContext).decrypt(sharedPreferencesManager.getPassword()) == inputPassword
    }

}
