package com.example.julen.nativeappsproject.note

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.example.julen.nativeappsproject.R
import com.example.julen.nativeappsproject.model.Note
import kotlinx.android.synthetic.main.activity_note_fragment.*

/**
 * In this activity will be displayed [NoteFragment] and [AddNoteFragment].
 *
 * @property mode represents if the user is viewing a note ([VIEW_NOTE]), adding a new note ([ADD_NOTE])
 * or editing a note([EDIT_NOTE])
 */
class NoteActivity : AppCompatActivity(), FragmentCommunication {

    /**
     * It overrides the interface [FragmentCommunication] in order to change the fragments.
     */
    override fun changeFragment() {
        mode = if(mode == ADD_NOTE || mode == EDIT_NOTE) VIEW_NOTE else EDIT_NOTE
        val FragmentToDisplay = if(mode == EDIT_NOTE)
            AddNoteFragment.newInstance()
        else
            NoteFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.noteFrame, FragmentToDisplay)
                .commit()
        invalidateOptionsMenu()
    }

    /**
     * Constant to manage the modes.
     */
    companion object {
        const val ADD_NOTE = "add_note"
        const val VIEW_NOTE = "view_note"
        const val NOTE_MODE = "note_mode"
        const val EDIT_NOTE = "edit_note"
    }

    private lateinit var mode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_fragment)

        setSupportActionBar(toolbar_note)

        //For back button
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mode = intent.getStringExtra(NOTE_MODE)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val FragmentToDisplay = if(mode == ADD_NOTE || mode == EDIT_NOTE)
                AddNoteFragment.newInstance(intent.getSerializableExtra(NoteFragment.ARG_NOTE) as Note)
            else
                NoteFragment.newInstance(
                        intent.getSerializableExtra(NoteFragment.ARG_NOTE) as Note
                )
            supportFragmentManager.beginTransaction()
                    .add(R.id.noteFrame, FragmentToDisplay)
                    .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Makes visible the menu items.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        if(mode == ADD_NOTE || mode == EDIT_NOTE){
            menu?.findItem(R.id.action_save)?.isVisible = true
            menu?.findItem(R.id.action_edit)?.isVisible = false
            menu?.findItem(R.id.action_add_note)?.isVisible = false
        }else{
            menu?.findItem(R.id.action_edit)?.isVisible = true
            menu?.findItem(R.id.action_save)?.isVisible = false
            menu?.findItem(R.id.action_add_note)?.isVisible = false
        }
        return true
    }
}
